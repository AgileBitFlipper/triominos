package com.thirdsonsoftware;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class EventManager implements Serializable {

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

    public static ArrayList<Event> getEvents() {
        return getInstance().events ;
    }

    public void logEvent(Event e) {
        events.add(e);
    }

    static void clearEvents() {
        getInstance().getEvents().clear();
    }

    static String logEvents() {
        String filename = "";
        try {
            Date dt = new Date();
            filename = String.format("logs/events_%s.ser",dt.hashCode());
            boolean f = new File("logs").mkdirs();
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (Event evt : getInstance().events) {
                oos.writeObject(evt);
            }
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error event serialization file not found");
        } catch (IOException e) {
            System.out.println("Error initializing event stream");
        }
        return filename ;
    }

    static void dumpEventData(String filename) {
        try {
            ArrayList<Event> evtList = new ArrayList<Event>();
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                Event evt;
                do {
                    evt = (Event) ois.readObject();
                    if (evt != null)
                        evtList.add(evt);
                } while (evt != null);
            } catch (EOFException eof) {
                System.out.println(String.format("  Reading of event log %s is complete.", filename));
            }
            ois.close();
            fis.close();

            System.out.println(String.format("   Events (%d) recorded:",evtList.size()));
            for ( Event e : evtList )
            {
                System.out.println(String.format("    %s", e));
            }
        } catch (FileNotFoundException e) {
            System.out.println(String.format("  Error event serialization file, %s, not found",filename));
        } catch (IOException e) {
            System.out.println("  Error initializing event stream");
            System.out.println("   Exception:"+e.toString());
        } catch (ClassNotFoundException cnfe ) {
            System.out.println("  Class not found in event stream");
        }
    }
}
