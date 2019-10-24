package com.thirdsonsoftware;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventManager implements Serializable {

    static final String EVENT_OBJECT_PATH_FORMAT = "logs/events%s.ser" ;
    static final String EVENT_OBJECT_PATH = "logs" ;
    static final String EVENT_OBJECT_FILE_PREFIX = "event" ;
    static final String EVENT_OBJECT_FILE_EXTENSION = ".ser" ;
    static final String EVENT_JSON_FILE_EXTENSION = ".json" ;

    ArrayList<Event> events ;

    private static EventManager instance ;

    //private constructor to avoid client applications to use constructor
    private EventManager(){
        events = new ArrayList<Event>() ;
    }

    public static EventManager getInstance(){
        if(instance == null){
            instance = new EventManager();
        }
        return instance;
    }

    /**
     * Return the current list of events
     * @return
     */
    public ArrayList<Event> getEvents() {
        return getInstance().events ;
    }

    /**
     * Log a specific event
     * @param e - the Event to log
     */
    public void logEvent(Event e) {
        events.add(e);
    }

    /**
     * Removes all events from the current event list
     */
    public void clearEvents() {
        getInstance().getEvents().clear();
    }

    /**
     * Return a unique event data file name
     * @return
     */
    public String getUniqueEventDataFilename() {
        Date dt = new Date();
        String filename = String.format(EVENT_OBJECT_PATH_FORMAT,dt.hashCode());
        return filename ;
    }

    /**
     * Take the current list of events and log them out to an Event file.
     * @return
     */
    public String logEvents() {

        // Create the log path in case it isn't there
        boolean f = new File(EVENT_OBJECT_PATH).mkdirs();

        // Obtain a unique event data file name based on our time code
        String filename = getUniqueEventDataFilename();

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Event evt : getInstance().events)
                oos.writeObject(evt);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.Error("  Error event serialization file not found:\n" + e.getMessage());
        } catch (IOException e) {
            Log.Error("  Error initializing event stream:\n" + e.getMessage());
        }
        return filename ;
    }

    /**
     * Collect all of the event data files and return them in a List
     * @return - a list of all event data files
     */
    public List<String> getAllEventDataFiles() {
        List<String> eventFiles = new ArrayList<String>();
        File index = new File(EVENT_OBJECT_PATH);
        String[] entries = index.list();
        for (String s : entries) {
            File currentFile = new File(index.getPath(), s);
            String filename = currentFile.getName();
            // todo: can we be smarter about this with a file system call that takes a mask?
            if (filename.startsWith(EVENT_OBJECT_FILE_PREFIX) &&
                    filename.endsWith((EVENT_OBJECT_FILE_EXTENSION)))
                eventFiles.add(currentFile.getAbsolutePath());
        }
        return eventFiles;
    }

    /**
     * Given a data filename, collect all the Event records and return
     *   them in a List
     * @param dataFile - the full path of the Event data file
     * @return - List of Event objects in the dataFile provided
     */
    public List<Event> getAllEventsForDataFile(String dataFile) {

        // The event list to return
        ArrayList<Event> evtList = new ArrayList<Event>();

        try {
            FileInputStream fis = new FileInputStream(dataFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                Event evt;
                while ( ( evt = (Event) ois.readObject() ) != null ) {
                    evtList.add(evt);
                }
            } catch (EOFException eof) {
                Log.Debug(String.format("  Reading of event log %s is complete.", dataFile));
            }
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Log.Error(String.format("  Error event serialization file, %s, not found", dataFile));
        } catch (IOException e) {
            Log.Error("  Error initializing event stream:\n" + e.getMessage());
        } catch (ClassNotFoundException cnfe ) {
            Log.Error("  Class not found in event stream:\n" + cnfe.getMessage());
        }
        return evtList ;
    }

    /**
     * Spin through the data files and dump the data
     */
    public void dumpAllEventData() {
        List<String> eventFiles = getAllEventDataFiles();
        for (String s : eventFiles) {
            File currentFile = new File(s);
            if (currentFile.getName().startsWith(EVENT_OBJECT_FILE_PREFIX)) {
                dumpEventData(currentFile.getAbsolutePath());
                dumpEventDataToFile(currentFile.getAbsolutePath());
            }
        }
    }

    /**
     * Given the event datafile name, dump all the records contained therein
     * @param filename - the name of the data file to dump
     */
    public void dumpEventData(String filename) {
        List<Event> events = getAllEventsForDataFile(filename);
        Log.Info(String.format("   Events (%d) recorded in %s:",events.size(),filename));
        for ( Event e : events )
            Log.Debug(String.format("    %s", e));
    }

    public void dumpEventDataToFile(String filename) {
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(filename.replace(".ser",".txt"));
            bw = new BufferedWriter(fw);
            List<Event> events = getAllEventsForDataFile(filename);
            Log.Debug(String.format("   Events (%d) recorded:",events.size()));
            for ( Event e : events )
                bw.write(String.format("    %s\n", e));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * This method is designed to scan the logs directory, looking for all event-*.ser files
     *   and making sure they have a proper event-*.json file.  These json files are used for analysis
     *   and contain almost an exact replicate of the ser files.
     */
    public void convertToJSON() {

        // Where do we find the log files we are to convert?
        List<String> entries = getAllEventDataFiles();

        for ( String eventFile : entries ) {

            String jsonFilename = eventFile.replace(EVENT_OBJECT_FILE_EXTENSION, EVENT_JSON_FILE_EXTENSION);

            // Let's setup the File object to reference the JSON file
            File jsonFile = new File(jsonFilename);

            if ( !jsonFile.exists() ) {

                List<Event> events = getAllEventsForDataFile(eventFile);

                writeJSONEventFile(events, jsonFile.getAbsolutePath());

            } else {
                Log.Debug(String.format("  Event file %s has already been translated to JSON.", eventFile));
            }
        }
    }

    /**
     * Takes the list of Events and writes out each of them to the JSON file provided
     * @param events - the list of Events
     * @param jsonFile - the JSON file to create and add all events to
     */
    public void writeJSONEventFile(List<Event> events, String jsonFile) {

        // Setup the JSON object mapper first
        ObjectMapper objectMapper = new ObjectMapper();

        // We need to define a serializer to help convert an Event to a JSON string
        SimpleModule eventModule = new SimpleModule("CustomEventSerializer", new Version(1, 0, 0, null, null, null));
        SimpleModule playerModule = new SimpleModule("CustomPlayerSerializer", new Version(1,0,0,null,null,null));
        SimpleModule tileModule = new SimpleModule("CustomTileSerializer", new Version(1,0,0,null,null,null));

        // Add the serializers to the module
        eventModule.addSerializer(Event.class, new CustomEventSerializer());
        playerModule.addSerializer(Player.class,new CustomPlayerSerializer());
        tileModule.addSerializer(Tile.class,new CustomTileSerializer());

        // Update the object mapper
        objectMapper.registerModule(eventModule);
        objectMapper.registerModule(playerModule);
        objectMapper.registerModule(tileModule);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            // Now, spin up the JSON factory and write the entries out.
            JsonFactory factory = new JsonFactory();
            JsonGenerator gen = factory.createGenerator( new File(jsonFile), JsonEncoding.UTF8);
            gen.useDefaultPrettyPrinter();

            try {

                // Take the entire list of events and write it out as a single string
                String obj = objectMapper.writeValueAsString(events);
                gen.writeRaw(obj);

            } catch ( Exception e ) {
                Log.Error("  Exception during object write: " + e.getMessage());
            } finally {
                if (gen != null) gen.close();
            }
        } catch (IOException e) {
            Log.Error("  Error initializing event stream:\n" + e.toString());
        }
    }
}
