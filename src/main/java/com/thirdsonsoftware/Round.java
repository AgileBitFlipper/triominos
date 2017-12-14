package com.thirdsonsoftware;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The game of Triominos is composed of a series of Rounds.
 *   Each Round ends when either a player reaches over 400 points,
 *   or all possible plays for each player have been completed.
 *   The game ends when a player reaches over 400 points.
 */
public class Round {

    static public final int TWO_PLAYER_DRAWS = 9 ;
    static public final int UP_TO_FOUR_PLAYER_DRAWS = 7 ;

    static public final int BONUS_EMPTY_TRAY = 25 ;

    // The number of draws are dependent on the number of players
    private int numDraws;

    // The index of this round
    private final int index ;

    // The board setup for each round
    private final Board board ;

    // The list of players playing the game
    private ArrayList<Player> players ;

    // The game has a pool of tray
    private ArrayList<Tile> tiles ;

    // Setup the played pieces queue
    private ArrayList<Tile> piecesPlayed ;

    // Setup for pieces that have faces empty
    private ArrayList<Tile> piecesOnBoardWithEmptyFaces ;

    public Round( int index, ArrayList<Player> players ) {

        // Setup the index
        this.index = index ;

        // Let's keep the list of players for each round
        this.players = players ;

        // Setup the board to place the tiles
        board = new Board();

        // Allocate enough space for the tile pool
        setTiles(new ArrayList<Tile>(56));

        // Setup the played pieces queue
        setPiecesPlayed(new ArrayList<Tile>(56));

        // Setup for pieces that have faces empty
        setPiecesOnBoardWithEmptyFaces(new ArrayList<Tile>(56));

        // Set the number of draws based on the number of players
        setNumDraws(players.size());

        // Generate the tray and put them in the pool
        Log.Info(" Generating tiles...");

        generateTiles();

    }

    protected ArrayList<Player> hasWon() {
        ArrayList<Player> pWon = new ArrayList<Player>();
        for ( Player p : getPlayers() ) {
            if ( p.getScore() >= 400 ) {
                pWon.add(p);
                break;
            }
        }
        return pWon ;
    }

    protected Player hasAnEmptyTray(){
        Player hasEmptyTray = null ;
        for ( Player p : getPlayers() ) {
            if (p.getTray().isEmpty()) {
                hasEmptyTray = p;
                break;
            }
        }
        return hasEmptyTray ;
    }

    protected int pointsTotalFromOtherPlayersTrays(Player winner) {
        int points = 0 ;
        for ( Player p : getPlayers() ) {
            if ( p != winner ) {
                for ( Tile t : p.getTray() ) {
                    points += t.getValue() ;
                }
            }
        }
        return points;
    }

    protected Player getPlayerWithFewestTiles() {
        Player pFewest = null;
        for ( Player p : getPlayers() ) {
            if ( pFewest == null || p.getTray().size() < pFewest.getTray().size() )
                pFewest = p ;
        }
        return pFewest;
    }

    /**
     * The main gameplay loop, looping through players and making plays based
     *   on the rules of the game.
     */
    public void play() {

        int indexPlayer ;
        int turn = 1 ;

        Player player ;
        Tile tilePlayed ;

        // Shuffle tile pool
        shuffleTilePool();

        // Draw the inital tiles for each player from the tile pool
        drawTiles();

        // The player with the highest value goes first
        Player firstPlayer = whoIsFirst();

        indexPlayer = players.indexOf(firstPlayer);
        player = firstPlayer ;

        // Let's show the game board to everyone!
        Log.Info(this.toString());

        // Gameplay continues until all players can't play
        int blockedPlayerCount = 0 ;
        boolean playBlocked = false ;
        while ( !playBlocked && ( hasAnEmptyTray() == null ) && ( hasWon().size() == 0 ) ) {

            Log.Info(String.format(" Turn %d by %s ...",turn++,player.getName()));

            // Keep running through the players
            tilePlayed = player.playATile(getBoard(),getPiecesPlayed(),getPiecesOnBoardWithEmptyFaces());

            // If the player played a tile, let's update the necessary elements
            if ( tilePlayed != null ) {

                // Add the newly played piece to the list of pieces played
                piecesPlayed.add(tilePlayed);

                // Show the board
                Log.Info(this.toString());

                // reset the blocked player count
                blockedPlayerCount = 0 ;

            } else {

                // Are there tiles left to play?
                if ( !getTiles().isEmpty() ) {

                    // Deduct 5 points
                    player.setScore(player.getScore() - 5);

                    Log.Info("   Unable to play a tile, deducting 5 points and drawing another tile.");
                    Log.Info( String.format("Player %s's score is now %d.",player.getName(),player.getScore()));

                    // Choose a new tile for the player
                    player.drawTile(tiles);

                    // Same player has to keep playing untl they get a pick or the well is dry.
                    continue ;

                } else {

                    // If there are no more tiles in the well deduct 10 points.
                    player.setScore(player.getScore() - 10);

                    Log.Info("   Unable to play a tile, and more more tiles in the pool.  Deducting 10 points.");
                    Log.Info( String.format("Player %s's score is now %d.",player.getName(),player.getScore()));
                }

                // Let's start incrementing the blocked count
                blockedPlayerCount += 1 ;
            }

            // Choose the next player
            indexPlayer = ( indexPlayer + 1 ) % players.size();
            player = players.get(indexPlayer);

            // If we've been to every player, and no one can play a tile, we are blocked
            if ( blockedPlayerCount >= players.size() )
                playBlocked = true ;
        }

        // If a player has an empty tray, they have won the round.
        //   Otherwise it is a draw.
        Player pRoundWinner = hasAnEmptyTray() ;
        if ( pRoundWinner != null ) {
            pRoundWinner.setScore(pRoundWinner.getScore()+BONUS_EMPTY_TRAY);
            pRoundWinner.setScore(pRoundWinner.getScore()+pointsTotalFromOtherPlayersTrays(pRoundWinner));
            pRoundWinner.setWonAGameCount(pRoundWinner.getWonAGameCount()+1);
            Log.Info(String.format("  Player '%s' won this round by playing all of their tiles.", pRoundWinner.getName()));

        // Else, the player with the fewest tiles at the end of the Round wins and
        // gets the value of all the tiles in the other player's trays.
        } else {
            pRoundWinner = getPlayerWithFewestTiles() ;
            pRoundWinner.setScore(pRoundWinner.getScore()+pointsTotalFromOtherPlayersTrays(pRoundWinner));
            Log.Info( String.format("  Player '%s' won this round with the fewest tiles remaining in their tray.", pRoundWinner.getName()));
        }

        // Do we have a winner of the game yet?
        Player pWinner = null ;

        // Now if a player has pushed over 400 points total for all rounds, they won the game.
        ArrayList<Player> pOver400 = hasWon() ;
        if ( pOver400 != null ) {

            // If more than one player is over 400, the winner of the round wins.
            if (pOver400.size() > 1) {

                Log.Info( "  More than one Player scored above 400 points!");

                // If the winner of the round is not one of the Players over 400, the
                //   higher point over 400 wins.
                if (!pOver400.contains(pRoundWinner)) {

                    int highestScore = 0;
                    for (Player p : pOver400) {
                        if (p.getScore() > highestScore) {
                            highestScore = p.getScore();
                            pWinner = p;
                        }
                    }
                    Log.Info("  Since the Player that won the round is not in the list of Players over 400 points, the Player with the highest points over 400 will win.");
                }
            } else if ( pOver400.size() == 1 ){
                Log.Info( "  Only one player is above 400 points.");
                pWinner = pOver400.get(0);
            } else {
                pWinner = null;
            }
        }

        // The Round is over...let's account for points.
        Log.Info("\n\n ==== ROUND OVER === \n\n");
        Log.Info(this.toString());

        if ( pWinner != null )
            Log.Info(String.format("Player '%s' has won the game by scoring %d points!  Congratulations!",pWinner.getName(),pWinner.getScore() ) ) ;

    }

    /**
     * The complete list of tiles (56) for this game.
     * @return (ArrayList<Tile>) the array list of tiles in the game.
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Sets up the ArrayList of tiles in the game.
     * @param tiles
     */
    protected void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Returns back the ArrayList of pieces played on the boad.
     * @return (ArrayList<Tile>) - the list of pieces played.
     */
    protected ArrayList<Tile> getPiecesPlayed() {
        return piecesPlayed;
    }

    /**
     * Sets the ArrayList of pieces played on the boad.
     * @return (ArrayList<Tile>) - the list of pieces played.
     */
    protected void setPiecesPlayed(ArrayList<Tile> piecesPlayed) {
        this.piecesPlayed = piecesPlayed;
    }

    /**
     * A list of tiles on the board that have at least one empty face.
     *   This speeds up the search for pieces that the player can play off of.
     * @return
     */
    protected ArrayList<Tile> getPiecesOnBoardWithEmptyFaces() {
        return piecesOnBoardWithEmptyFaces;
    }

    /**
     * Sets the number of pieces on the board with empty faces.
     * @param piecesOnBoardWithEmptyFaces
     */
    protected void setPiecesOnBoardWithEmptyFaces(ArrayList<Tile> piecesOnBoardWithEmptyFaces) {
        this.piecesOnBoardWithEmptyFaces = piecesOnBoardWithEmptyFaces;
    }

    /**
     * Returns the current board for the game.
     * @return (Board) the current board
     */
    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Set the totaly number of draws per player
     * @param numPlayers (int) number of players in the game
     */
    public void setNumDraws(int numPlayers) {
        // Set the number of draws based on the number of players
        if (numPlayers <= 2) {
            this.numDraws = TWO_PLAYER_DRAWS;
        } else {
            this.numDraws = UP_TO_FOUR_PLAYER_DRAWS;
        }
    }

    /**
     * How many draws does each player start with?
     * @return (int) number of draws per player
     */
    public int getNumDraws() {
        return numDraws;
    }

    /**
     * Spins through the players and performs a draw to pull the inital
     *   number of tiles that each player starts with
     */
    protected void drawTiles() {

        // Draw tray for each player, taking turns
        Log.Info(" Drawing tiles for each player's tray...");
        for ( int draw = 0; draw < getNumDraws(); draw++ ) {
            Log.Info(String.format("  Draw %s", draw ));
            for ( Player p : getPlayers() ) {
                p.drawTile(getTiles());
            }
        }
    }

    /**
     * Shuffles the tiles in the tile pool so that we randomize the picking order.
     */
    protected void shuffleTilePool() {

        // Shuffle the tray for randomized picking
        Log.Info(" Shuffelling tile pool...");
        Collections.shuffle(tiles);
    }
    /**
     * Scans through the list of players, looking at each ones tray,
     *   and determines who has the highest value tile.
     * The player with the highest triplet starts first.  If no player
     *   has a triplet, then the player with the highest value tile
     *   goes first.  The player scores the value of the tile plus a bonus
     *   of 10 points.
     * If the player has both the highest triplet and the "0-0-0" tile
     *   they would play the 0's tile first for a bonus of 30 additional
     *   points.
     * @return the player that starts the game
     */
    protected Player whoIsFirst() {

        // By default, the first player is the first in the list
        Player first = players.get(0);

        Tile tile ;
        Tile startTile = null;
        Tile highestValue = null ;
        Tile highestTriplet = null ;

        // Spin through each player looking for the highest value
        //   tile.
        for ( Player p : players ) {

            tile = p.determineFirstTile() ;

            if ( tile.getValue() == 0 ) {
                Log.Info(String.format("  Player '%s' has tile '%s'!", p.getName(), tile ));
                startTile = tile ;
                first = p ;
                break;
            } else if ( tile.isTriplet() ) {
                if ( ( highestTriplet == null ) ||
                        ( tile.getValue() > highestTriplet.getValue() ) ) {
                    Log.Info(String.format("  Player '%s' has highest triplet tile '%s' so far...", p.getName(), tile ));
                    startTile = tile ;
                    highestTriplet = tile ;
                    first = p ;
                }
            } else {
                if ( ( highestTriplet == null ) &&
                        ( ( highestValue == null )  ||
                                ( tile.getValue() > highestValue.getValue() ) ) ) {
                    Log.Info(String.format("  Player '%s' has highest value tile '%s' so far...", p.getName(), tile ));
                    startTile = tile ;
                    highestValue = tile ;
                    first = p ;
                }
            }
        }

        // Setup the starting player and mark them appropriately
        for ( Player p : players )
            p.setStarts( p == first );

        Log.Info(String.format("  Player '%s' will start with tile '%s'.",first.getName(),startTile));
        return first;
    }

    /**
     * Generate the tile pool for the players to pull from.
     * Name: generateTiles
     * Desc: Designed to build the complete pool of 56 tray
     *       for the players to pull from.  The game triominos
     *       has two different versions of the tray, the forward
     *       and two-reversed tile formats.  These tray follow a
     *       Pattern of non-repeating clockwise ordering of the
     *       corner values.
     * Note: No three corners repeat with another tile.  Values
     *       4-5-4 and 4-5-5 appear only once.
     */
    protected void generateTiles() {                     // The 56-pieces generated should match this table
        int id = 1 ;                                     // -----------------------------------------------
        int cStart ;                                     // 01 0-0-0 1-1-1 2-2-2 3-3-3 4-4-4 5-5-4
        // 02 0-0-1 1-1-2 2-2-3 3-3-4 4-4-5 5-5-5
        for (int a = 0; a <= 5; a++) {                   // 03 0-0-2 1-1-3 2-2-4 3-3-5
            for (int b = a; b <= 5; b++) {               // 04 0-0-3 1-1-4 2-2-5 3-4-4
                if (a == 5) {                            // 05 0-0-4 1-1-5 2-3-3 3-4-5
                    cStart = 4;                      // 06 0-0-5 1-2-2 2-3-4 3-5-5
                } else if (a == 4 && b == 5) {           // 07 0-1-1 1-2-3 2-3-5
                    continue;                            // 08 0-1-2 1-2-4 2-4-4
                } else {                                 // 09 0-1-3 1-2-5 2-4-5
                    cStart = b;                          // 10 0-1-4 1-3-3 2-5-5
                }                                        // 11 0-1-5 1-3-4
                for (int c = cStart; c <= 5; c++) {      // 12 0-2-2 1-3-5
                    Tile tile = new Tile(a, b, c);       // 13 0-2-3 1-4-4
                    tile.setId(id++);                    // 14 0-2-4 1-4-5
                    tile.setRotation(0);                 // 15 0-2-5 1-5-5
                    tile.rotate(0);                    // 16 0-3-3
                    tile.setOrientation(Orientation.UP); // 17 0-3-4
                    tiles.add(tile);                     // 18 0-3-5
                }                                        // 19 0-4-4
            }                                            // 20 0-4-5
        }                                                // 21 0-5-5
    }


    /**
     * Accepts a title and an array list of tiles and builds a String
     *  containing a bracketed list of the tiles.
     * @param name - Name to display to the left of the list
     * @param list - The list of tiles to display
     * @return String containing the name and bracketed list of Tiles
     */
    protected String displayTiles(Boolean asTile, String name, ArrayList<Tile> list) {
        StringBuilder strTiles = new StringBuilder();
        strTiles.append(String.format("%s (%d):\n", name, list.size()));
        if (list.isEmpty()) {
            strTiles.append("  [<empty>]\n");
        } else {
            if ( asTile ) {
                String rows[] = new String[5];
                Orientation o;
                int r;
                for (int i=0;i<5;i++)
                    rows[i] = "  ";
                for (Tile tile : list) {
                    o = tile.getOrientation();
                    r = tile.getRotation();
                    tile.setOrientation(Orientation.UP);
                    tile.setRotation(0);
                    tile.draw(true, rows);
                    tile.setOrientation(o);
                    tile.setRotation(r);
                }
                for (String str:rows)
                    strTiles.append(str+"\n");
            } else {
                strTiles.append("  [");
                for (Tile tile : list) {
                    if (list.lastIndexOf(tile) != list.size() - 1)
                        strTiles.append(tile).append(", ");
                    else
                        strTiles.append(tile);
                }
                strTiles.append("]\n");
            }
        }
        return strTiles.toString();
    }

    /**
     * Displays the entire Tile pool contents
     * @return - String containing the complete Tile pool contents.
     */
    protected String displayTilePool() {
        return displayTiles(true," Tile Pool", tiles);
    }

    /**
     * Displays the complete list of played tiles
     * @return - String containing the complete list of played tiles.
     */
    protected String displayPlayedTiles() {
        return displayTiles(true, " Played Tiles", piecesPlayed);
    }

    /**
     * Displays the tiles on the board that still have open faces (playable)
     * @return - String containing the list of tiles that still have at least one open face.
     */
    protected String displayTilesWithFacesRemaining() {
        return displayTiles( true," Pieces on Board with Empty Faces", piecesOnBoardWithEmptyFaces );
    }


    /**
     * Displays both the player count, and each player in the game.
     * @return - String containing the count and list of players.
     */
    protected String displayPlayers() {
        StringBuilder playersString = new StringBuilder(200);
        playersString.append(" Players (").append(players.size()).append("):\n");
        for ( Player p : players ) {
            playersString.append(p);
        }
        return playersString.toString();
    }

    /**
     * Provides a text view of the current game, including the Tile pool,
     *   the player list, and the current board.
     * @return - String containing a snapshot view of the current Game.
     */
    public String toString() {
        return ("Round " + this.index + ":\n" +
                displayPlayers() +
                displayTilePool() +
                displayPlayedTiles() +
                board +
                displayTilesWithFacesRemaining());
    }

}
