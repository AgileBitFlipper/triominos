package com.thirdsonsoftware

class EventTest extends GroovyTestCase {

    static final String EXPECTED_EVENT         = "Type:START_A_GAME,Game:1,Round:0,Name:,Tile:,Position:(0,0),,Score:0,Starts:false,Start Bonus:0,Completed Bridge:false,Completed Hexagon:false,End of Round:false,End of Game:false"
    static final String EXPECTED_EVT_W_TILE    = "Type:PLACE_A_TILE,Game:1,Round:2,Name:Andy,Tile:0-0-0,Position:(0,0),,Score:0,Starts:false,Start Bonus:0,Completed Bridge:false,Completed Hexagon:false,End of Round:false,End of Game:false"
    static final String EXPECTED_RND_NUM_EVENT = "Type:END_A_ROUND,Game:1,Round:1,Name:,Tile:,Position:(0,0),,Score:0,Starts:false,Start Bonus:0,Completed Bridge:false,Completed Hexagon:false,End of Round:false,End of Game:false"
    static final String EXPECTED_EVT_W_ROUND   = "Type:GENERATE_TILES,Game:1,Round:2,Name:,Tile:,Position:(0,0),,Score:0,Starts:false,Start Bonus:0,Completed Bridge:false,Completed Hexagon:false,End of Round:false,End of Game:false"
    static final String EXPECTED_START_EVENT   = "Type:SETUP_PLAYERS,Game:1,Round:0,Name:,Tile:,Position:(0,0),,Score:0,Starts:false,Start Bonus:0,Completed Bridge:false,Completed Hexagon:false,End of Round:false,End of Game:false"

    Tile tileA = new Tile(0,0,0)

    Player playerAndy    = new Player("Andy")
    Player playerBilly   = new Player("Billy")
    Player playerChrissy = new Player("Chrissy")

    ArrayList<Player> players = new ArrayList<Player>() ;

    Event eventA = new Event(EventType.START_A_GAME)

    void setUp() {
        super.setUp()

        players.add(playerAndy)
        players.add(playerBilly)
        players.add(playerChrissy)

    }

    void tearDown() {
        cleanLogs()
        EventManager.getInstance().clearEvents();
    }

    void cleanLogs() {
        // Clean up the logs from any other runs
        //   so we can use them for testing.
        File index = new File(EventManager.EVENT_OBJECT_PATH)
        String[]entries = index.list()
        for(String s: entries){
            File currentFile = new File(index.getPath(),s)
            if ( currentFile.getName().startsWith(EventManager.EVENT_OBJECT_FILE_PREFIX) )
                currentFile.delete()
        }
    }

    String stripDateTime( String str ) {
        // Split the string up by ','
        String[] items = str.split(",")

        // The first part of the string is the DateTime sequence
        String strNew = str.replace(items[0]+",","")

        // Return the stripped down string
        return strNew
    }

    String getEventFromLog(int eventNumber ) {

        // The event string we are going to return
        String strEvent = "" ;

        // Load the list of events from the file.
        ArrayList<Event> eventList = new ArrayList<Event>();

        // Find the logs we just created.
        File index = new File(EventManager.EVENT_OBJECT_PATH)

        // Load up the full list of event files
        // We are going to assume there is only one since
        //   setup removes all of the others
        String[] entries = index.list()
        for (String s : entries) {

            // Let's make a reference to the file
            File currentFile = new File(index.getPath(), s)
            if (currentFile.getName().startsWith(EventManager.EVENT_OBJECT_FILE_PREFIX)) {

                // Process the objects in the file now.
                FileInputStream fis = new FileInputStream(currentFile.getAbsolutePath())
                ObjectInputStream ois = new ObjectInputStream(fis)
                try {

                    // Read the object from the file
                    Event evt = (Event) ois.readObject()
                    if (evt != null)
                        eventList.add(evt);

                } catch (EOFException eof) {
                    // End of file reached...
                }
                ois.close()
                fis.close()
            }
        }

        // The asking index needs to be less than the number of
        //   events we loaded from the file
        if ( eventNumber < eventList.size() ) {
            strEvent = eventList[eventNumber].toString()
        }

        return strEvent
    }

    void testToString() {
        // We have to do some creative cutting to remove the date/time string
        //   from the lead of the string.  The Date will always NOT match otherwise.
        String strEvent = stripDateTime(eventA.toString())
        assertEquals(EXPECTED_EVENT, strEvent )
    }

    // static void logEvent( Event evt )
    void testLogEvent() {
        Event.logEvent(eventA)
        EventManager.getInstance().logEvents()
        String strEvent = getEventFromLog(0)
        assertEquals(EXPECTED_START_EVENT, stripDateTime(strEvent))
    }

    // static void logEvent( EventType type )
    void testLogEventByEvent() {
        Event.logEvent(EventType.START_A_GAME)
        EventManager.getInstance().logEvents()
        String strEvent = getEventFromLog(0)
        assertEquals(EXPECTED_EVENT, stripDateTime(strEvent))
    }

    // static void logEvent( EventType type, int round )
    void testLogEventWithRoundNumber() {
        Event.logEvent(EventType.END_A_ROUND,1)
        EventManager.getInstance().logEvents()
        String strEvent = getEventFromLog(0)
        assertEquals(EXPECTED_RND_NUM_EVENT, stripDateTime(strEvent))
    }

    // static void logEvent( EventType type, Round r )
    void testLogEventWithRound() {
        Round round = new Round(2,players)
        Event.logEvent(EventType.END_A_ROUND,round)
        EventManager.getInstance().logEvents()
        String strEvent = getEventFromLog(0)
        assertEquals(EXPECTED_EVT_W_ROUND, stripDateTime(strEvent))
    }

    // static void logEvent( EventType type, Tile t, Player p, int round)
    void testLogEventWithTilePlayerAndRound() {
        Event.logEvent(EventType.PLACE_A_TILE, tileA, playerAndy, 2)
        EventManager.getInstance().logEvents()
        String strEvent = getEventFromLog(0)
        assertEquals(EXPECTED_EVT_W_TILE, stripDateTime(strEvent))
    }
}
