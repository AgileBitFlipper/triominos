/*
 * Copyright (c) 2017 Third Son Software, LLC
 * (http://thirdsonsoftware.com/) and others. All rights reserved.
 * Created by Andrew B. Montcrieff on 11/30/17 5:08 PM.
 *
 * This file is part of Project triominos.
 *
 * triominos can not be copied and/or distributed without the express
 * permission of Andrew B. Montcrieff or Third Son Software, LLC.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified: 11/30/17 4:59 PM
 */

package com.thirdsonsoftware;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("SpellCheckingInspection")
class Game implements Serializable {

    static public final int TWO_PLAYER_DRAWS = 9 ;
    static public final int UP_TO_FOUR_PLAYER_DRAWS = 7 ;

    private static final int DEFAULT_NUMBER_OF_PLAYERS = 2 ;
    private static final int UP_TO_FOUR_NUMBER_OF_PLAYERS = 4 ;

    private int numDraws; // setting it to zero is apparently redundant

    private int numPlayers; // how many players are playing this game?

    // Each game can have a number of players
    private ArrayList<Player> players;

    // The game has a pool of tray
    private ArrayList<Tile> tiles ;

    // Setup the played pieces queue
    private ArrayList<Tile> piecesPlayed ;

    // Setup for pieces that have faces empty
    private ArrayList<Tile> piecesOnBoardWithEmptyFaces ;

    // The tray can be played by a player on the board
    private final Board board ;

    /**
     * Construct the default game
     */
    Game(int numPlayers) {

        System.out.println("Let's play triominos!");

        // Setup the board to place the tiles
        board = new Board();

        setNumPlayers(numPlayers);

        // Decide how many tray each player draws
        if (numPlayers <= DEFAULT_NUMBER_OF_PLAYERS) {
            setNumDraws(TWO_PLAYER_DRAWS);
        } else if (numPlayers <= UP_TO_FOUR_NUMBER_OF_PLAYERS) {
            setNumDraws(UP_TO_FOUR_PLAYER_DRAWS);
        } else {
            throw new IllegalArgumentException(String.format("Invalid number of players: %d\nValue must be between 2 and 4.", numPlayers));
        }

        System.out.println(String.format(" Setting up for %d players.", numPlayers));

        // Allocate the number of players specified
        setPlayers(new ArrayList<Player>(numPlayers));

        // Assign player names
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(String.format("Player %c", 'A' + i )));
        }

        // Allocate enough space for the tile pool
        setTiles(new ArrayList<Tile>(56));

        // Setup the played pieces queue
        setPiecesPlayed(new ArrayList<Tile>(56));

        // Setup for pieces that have faces empty
        setPiecesOnBoardWithEmptyFaces(new ArrayList<Tile>(56));

        // Generate the tray and put them in the pool
        System.out.println(" Generating tiles...");

        generateTiles();
    }

    public int getNumDraws() {
        return numDraws;
    }

    public void setNumDraws(int numDraws) {
        this.numDraws = numDraws;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int index) {
        if ( ( index < players.size() ) && ( index >= 0 ) )
            return players.get(index) ;
        else
            return null;
    }

    protected void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    protected void setTiles(ArrayList<Tile> tiles) {
        this.tiles = tiles;
    }

    protected ArrayList<Tile> getPiecesPlayed() {
        return piecesPlayed;
    }

    protected void setPiecesPlayed(ArrayList<Tile> piecesPlayed) {
        this.piecesPlayed = piecesPlayed;
    }

    protected ArrayList<Tile> getPiecesOnBoardWithEmptyFaces() {
        return piecesOnBoardWithEmptyFaces;
    }

    protected void setPiecesOnBoardWithEmptyFaces(ArrayList<Tile> piecesOnBoardWithEmptyFaces) {
        this.piecesOnBoardWithEmptyFaces = piecesOnBoardWithEmptyFaces;
    }

    public Board getBoard() {
        return board;
    }

    protected void drawTiles() {

        // Draw tray for each player, taking turns
        System.out.println(" Drawing tiles for each player's tray...");
        for ( int draw = 0; draw < getNumDraws(); draw++ ) {
            System.out.println(String.format("  Draw %s", draw ));
            for ( Player p : getPlayers() ) {
                p.drawTile(getTiles());
            }
        }
    }

    protected void shuffleTilePool() {

        // Shuffle the tray for randomized picking
        System.out.println(" Shuffelling tile pool...");
        Collections.shuffle(tiles);
    }

    /**
     * Do you want to play a game?
     */
    public void play() {

        int indexPlayer ;
        int turn = 1 ;

        Player player ;
        Tile tilePlayed ;

        // Shuffle tile pool
        shuffleTilePool();

        // Fill the player trays with tiles
        drawTiles();

        // The player with the highest value goes first
        Player firstPlayer = whoIsFirst();

        indexPlayer = players.indexOf(firstPlayer);
        player = firstPlayer ;

        // Let's show the game board to everyone!
        System.out.println(this);

        // Gameplay continues until all players can't play
        int blockedPlayerCount = 0 ;
        boolean playBlocked = false ;
        while ( !playBlocked ) {

            System.out.println(String.format(" Turn %d by %s ...",turn++,player.getName()));

            // Keep running through the players
            tilePlayed = player.playATile(getBoard(),getPiecesPlayed(),getPiecesOnBoardWithEmptyFaces());

            // If the player can't play a tile, make them choose another
            if ( tilePlayed == null ) {

                // Are there tiles left to play?
                if ( !getTiles().isEmpty() ) {

                    // Deduct 5 points
                    player.setScore(player.getScore() - 5);

                    // Choose a new tile for the player
                    player.drawTile(tiles);

                    // Same player has to keep playing untl they get a pick or the well is dry.
                    continue ;

                } else {
                    // If there are no more tiles in the well deduct 10 points.
                    player.setScore(player.getScore() - 10);
                }

                // Let's start incrementing the blocked count
                blockedPlayerCount += 1 ;

            } else {

                // The first tile played
                if (getPiecesPlayed().size() == 0) {

                    // Starting player can earn 10 points if tile is a triplet
                    if (tilePlayed.isTriplet()) {

                        player.setScore(player.getScore() + 10);

                        // If three 0's start, there is a 30 point bonus
                        if (tilePlayed.getValue() == 0)
                            player.setScore(player.getScore() + 30);
                    }
                }

                // Add the newly played piece to the list of pieces played
                piecesPlayed.add(tilePlayed);

                // Add the points for the tile.
                player.setScore(player.getScore() + tilePlayed.getValue());

                // Need to add points for special plays
                // Complete a hexagon, get 50 points (plus tile value).
                // Forming a bridge, get 40 points (plus tile value).
                // Adding to a bridge, get 40 points (plus tile value).


                // Show the board
                System.out.println(this);

                blockedPlayerCount = 0 ;
            }

            // Choose the next player
            indexPlayer = ( indexPlayer + 1 ) % players.size();
            player = players.get(indexPlayer);

            // If no one can play a tile, we are blocked
            if ( blockedPlayerCount >= players.size() )
                playBlocked = true ;
        }

        // The game is over...let's account for points.
        System.out.println("\n\n ==== GAME OVER === \n\n");
        System.out.println(this);

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
                System.out.println(String.format("  Player '%s' has tile '%s'!", p.getName(), tile ));
                startTile = tile ;
                first = p ;
                break;
            } else if ( tile.isTriplet() ) {
                if ( ( highestTriplet == null ) ||
                        ( tile.getValue() > highestTriplet.getValue() ) ) {
                    System.out.println(String.format("  Player '%s' has highest triplet tile '%s' so far...", p.getName(), tile ));
                    startTile = tile ;
                    highestTriplet = tile ;
                    first = p ;
                }
            } else {
                if ( ( highestTriplet == null ) &&
                        ( ( highestValue == null )  ||
                          ( tile.getValue() > highestValue.getValue() ) ) ) {
                    System.out.println(String.format("  Player '%s' has highest value tile '%s' so far...", p.getName(), tile ));
                    startTile = tile ;
                    highestValue = tile ;
                    first = p ;
                }
            }
        }

        // Setup the starting player and mark them appropriately
        for ( Player p : players )
            p.setStarts( p == first );

        System.out.println(String.format("  Player '%s' will start with tile '%s'.",first.getName(),startTile));
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
        return displayTiles(true,"Tile Pool", tiles);
    }

    protected String displayPlayedTiles() {
        return displayTiles(true, "Played Tiles", piecesPlayed);
    }

    protected String displayTilesWithFacesRemaining() {
        return displayTiles( true,"Pieces on Board with Empty Faces", piecesOnBoardWithEmptyFaces );
    }

    /**
     * Displays both the player count, and each player in the game.
     * @return - String containing the count and list of players.
     */
    protected String displayPlayers() {
        StringBuilder playersString = new StringBuilder(200);
        playersString.append("Players (").append(players.size()).append("):\n");
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
        return ("Game:\n" +
                displayTilePool() +
                displayPlayers() +
                displayPlayedTiles() +
                board +
                displayTilesWithFacesRemaining());
    }

    public void setNumPlayers(int numPlayers) {
        if ( ( numPlayers >= 2) && ( numPlayers <= 4 ) ) {
            this.numPlayers = numPlayers ;
        } else {
            this.numPlayers = DEFAULT_NUMBER_OF_PLAYERS ;
        }
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

}
