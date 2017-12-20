package com.thirdsonsoftware;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

enum EventType {
    START_A_GAME,
    START_A_ROUND,
    SETUP_PLAYERS,
    GENERATE_TILES,
    SHUFFLE_TILES,
    SETUP_PLAYER_TRAY,
    HIGHEST_TILE_START,
    TRIPLE_ZERO_BONUS,
    TRIPLE_PLAY_BONUS,
    DRAW_A_TILE,
    PLACE_A_TILE,
    FAIL_CORNER_TEST,
    CREATE_A_HEXAGON,
    CREATE_A_BRIDGE,
    WIN_A_ROUND_BY_EMPTY_TRAY,
    WIN_A_ROUND_BY_FEWEST_TILES,
    WIN_A_GAME,
    END_A_ROUND,
    END_A_GAME,
}

public class Event implements Serializable {

    // When did the event occur
    private static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    Date eventDateTime ;

    // What type of event was this
    EventType type ;

    // Played by
    Player player ;

    // Tile played, including orientation and rotation
    Tile tile ;

    // Location played
    int row ;
    int col ;

    // Score received for play
    int score ;

    // Received a start bonus
    int startBonus ;
    boolean startingMove ;

    // Specialized moves
    boolean completedAHexagon ;

    // Bridge?
    boolean completedABridge ;

    // Final move
    boolean endOfRound ;

    // Final round
    boolean endOfGame ;

    // What was the current state of the round?
    //Round round ;
    int round ;

    // Generic Event
    public Event(EventType evtType) {
        type = evtType ;
        eventDateTime = new Date();
    }

    // Default constructor
    public Event(EventType evtType, Player p, Tile t, int r, int c) {
        eventDateTime = new Date();
        type = evtType ;
        player = p;
        tile = t;
        row = r;
        col = c ;
    }

    public String toString() {
        StringBuilder strEvent = new StringBuilder(100);
        strEvent.append(eventDateTime).append(",");
        strEvent.append("Type:").append(type).append(",");
        strEvent.append("Round:").append(round).append(",");
        strEvent.append("Name:").append((player!=null) ? player.getName():"").append(",");
        strEvent.append("Tile:").append((tile!=null) ? tile : "").append(",");
        strEvent.append(String.format("Position:(%d,%d),",row,col)).append(",");
        strEvent.append("Score:").append(score).append(",");
        strEvent.append("Starts:").append(startingMove ? "true":"false").append(",");
        strEvent.append("Start Bonus:").append(startBonus).append(",");
        strEvent.append("Completed Bridge:").append(completedABridge).append(",");
        strEvent.append("Completed Hexagon:").append(completedAHexagon).append(",");
        strEvent.append("End of Round:").append(endOfRound ? "true":"false").append(",");
        strEvent.append("End of Game:").append(endOfGame ? "true":"false").append(",");
        return strEvent.toString();
    }

    static void logEvent( Event evt ) {
        EventManager.getInstance().logEvent(evt);
    }

    static void logEvent( EventType type ) {
        Event event = new Event( type ) ;
        EventManager.getInstance().logEvent(event);
    }

    static void logEvent( EventType type, int round ) {
        Event event = new Event( type ) ;
        event.round = round ;
        EventManager.getInstance().logEvent(event);
    }

    static void logEvent( EventType type, Round r ) {
        Event event = new Event( type ) ;
        event.round = r.getRoundNumber() ;
        EventManager.getInstance().logEvent(event);
    }

    static void logEvent( EventType type, Tile t, Player p, int round) {
        Event event = new Event( type ) ;
        event.tile = t ;
        event.player = p ;
        event.round = round ;
        EventManager.getInstance().logEvent(event);
    }

}
