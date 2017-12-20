package com.thirdsonsoftware;

import java.time.LocalDate;

public class Play {

    // When
    LocalDate whenPlayed ;

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
    Round round ;

    // Default constructor
    public Play(Player p, Tile t, int r, int c) {

    }
}
