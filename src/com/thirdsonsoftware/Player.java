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

import java.util.ArrayList;
import java.util.Iterator;

public class Player {

    private int score ;               // The players score
    private boolean starts = false ;  // This player starts the game (highest tile)
    private String name ;             // The name of the player

    final ArrayList<Tile> tray;       // The tray in the player's hand

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStarts() {
        return starts;
    }

    public void setStarts(boolean starts) {
        this.starts = starts;
    }

    /**
     * @param name - name of player
     */
    public Player(String name) {
        setScore(0);
        setStarts(false);
        setName(name);
        tray = new ArrayList<>(56);
    }

    /**
     * Pop the top tile from the pool
     * Assumes the pool has been properly shuffled for
     *   randomness
     *
     * @param tilePool - the pool of tray to draw from
     */
    public boolean drawTile( ArrayList<Tile> tilePool ) {
        Tile t = tilePool.remove(0);
        System.out.println( String.format( "   Removing tile %s and adding it to %s's tray.", t, name ) );
        return tray.add(t);
    }

    /**
     * @return the highest valued tile in this players tray
     */
    public Tile highestValueTile() {

        Tile highestValue = null ;

        if ( tray.size() > 0 ) {
            for ( Tile t : tray) {
                if ( ( highestValue == null ) || ( t.getValue() > highestValue.getValue() ) ) {
                    highestValue = t;
                }
            }
        }
        return highestValue ;
    }

    /**
     * @return string that represents the Player's state
     */
    public String toString() {
        StringBuilder description = new StringBuilder(String.format("  Name: %s\n    Starts: %s\n    Score: %d\n    Hand: %s\n",
                name,
                starts ? "yes" : "no",
                score,
                tray));
        if ( starts ) {
            description.append("    Starting tile:\n");
            String[] tile_desc = new String[5];
            for ( int i=0;i<5;i++)
                tile_desc[i] = "      " ;
            highestValueTile().draw(true, tile_desc);
            for ( int i=0;i<5;i++)
                description.append(tile_desc[i]).append("\n");
        }
        return description.toString();
    }

    private Tile playFirstTile( Board board ) {

        int row ;
        int col ;
        Tile myTileToPlay = null;

        // Am I the player that starts?
        if ( getStarts() ) {

            myTileToPlay = highestValueTile();
            row = 56;
            col = 56;

            if ( board.placeTile(myTileToPlay,row,col) ) {

                myTileToPlay.setPlayer(this);

                System.out.println(String.format("   Played first tile '%s' at location (%d,%d).", myTileToPlay, row, col));

                // Now that we've played it, remove it from the players tray
                tray.remove(myTileToPlay);

                // Return back the tile we played so we can us it for choosing faces next time
                return myTileToPlay;
            }
        }

        return myTileToPlay ;
    }

    /*
     ================================= Game Board =======================================
    |                       1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 3 3 3 3 3 3 3 3 3 3 4|
    |     1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0|
     ====================================================================================
    |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ |
    | 1 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \|
    |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /|
    | 2 | \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ |
    |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ |
    | 3 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \|
    |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /|
    | 4 | \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ |
    |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ |
    | 5 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \|
    |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /|
    | 6 | \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ |
    |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ |
    | 7 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \|
    |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /|
    | 8 | \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ |
     ====================================================================================

    Row + Col == Even means tile points up
    Row + Col == Odd  means tile points down

    */

    private String displayComparisonOfTwoTiles(Tile a, Tile b) {

        StringBuilder strReturn = new StringBuilder();

        // Let's compare the two...
        String lines[] = new String[5];

        // Prefix for each line..
        lines[0] = "    Tiles: " ;
        lines[1] = "           " ;
        lines[2] = "           " ;
        lines[3] = "           " ;
        lines[4] = "           " ;

        // Adding the text for the first tile
        if ( a != null ) {

            a.draw(true,lines);

            // Now, adding the separator between the tiles
            if ( a.getOrientation() == Orientation.DOWN ) {
                lines[0] += " - ";
                lines[1] += " - ";
                lines[2] += "  - ";
                lines[3] += "   - ";
                lines[4] += "   - ";
            } else {
                lines[4] += " - ";
                lines[3] += " - ";
                lines[2] += "  - ";
                lines[1] += "   - ";
                lines[0] += "   - ";
            }

        } else {
            lines[0] += "      - " ;
            lines[1] += "      - " ;
            lines[2] += " none - " ;
            lines[3] += "      - " ;
            lines[4] += "      - " ;
        }

        if ( b != null ) {

            // Now adding the second tile...
            b.draw(true, lines);

        } else {
            lines[2] += " none " ;
        }

        // Move the String array to a string for display.
        for ( String strRow : lines )
            strReturn.append(strRow).append("\n");

        return strReturn.toString();
    }

    /**
     * @param board the board that a tile is placed on
     */
    public Tile playATile(Board board, ArrayList<Tile> playedTiles, ArrayList<Tile> tilesWithAvailableFaces ) {

        int row ;
        int col ;

        Tile tileToMatch = null ;
        Tile myTileToPlay ;

        // Is this the first tile played?
        if ( board.count() == 0 ) {

            myTileToPlay = playFirstTile(board);

        } else {

            myTileToPlay = null;

            Iterator<Tile> iTile = tilesWithAvailableFaces.iterator();

            // Let's look at our played tiles and see if any have available faces...
            while (iTile.hasNext()) {

                Tile played = iTile.next() ;

                int tileRow = played.getRow();
                int tileCol = played.getCol();

                Orientation playedTileOrientation = played.getOrientation() ;
                int directionToLook = ( playedTileOrientation == Orientation.UP ) ? 1 : -1 ;

                boolean bWeCanLookLeft  = ( tileCol > 0 );
                boolean bWeCanLookRight = ( tileCol < board.getNumberOfCols()-1 ) ;
                boolean bWeCanLookDown  = ( ( directionToLook > 0 ) && ( tileRow < board.getNumberOfRows()-1 ) );
                boolean bWeCanLookUp    = ( ( directionToLook < 0 ) && ( tileRow > 0 ) ) ;

                boolean bLeftFaceOpen = bWeCanLookLeft && ( board.playedTiles[tileRow][tileCol - 1] == null ) ;
                boolean bRightFaceOpen = bWeCanLookRight && ( tileCol > 0 ) && ( board.playedTiles[tileRow][tileCol + 1] == null ) ;
                boolean bMiddleFaceOpen = ( bWeCanLookDown || bWeCanLookUp ) && ( board.playedTiles[tileRow + directionToLook][tileCol] == null ) ;

                // Look left on the board to see if there is an empty space
                if ( bLeftFaceOpen ) {

                    row = tileRow;
                    col = tileCol - 1;

                    // Ok, so we have a space to the left, let's get the left face for this played tile
                    Face leftFace = played.getLeftFace();

                    // Does the left face match a face on any of our tile's faces?
                    for (Tile trayTile : tray) {

                        // Always compare with a known orientation to make things simpler
                        trayTile.setOrientation(Orientation.UP);

                        // Assume we are going to play this one
                        myTileToPlay = trayTile;

                        // Compare all the faces
                        if (trayTile.getLeftFace().match(leftFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 120 : 240);
                        } else if (trayTile.getRightFace().match(leftFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 240 : 0);
                        } else if (trayTile.getMiddleFace().match(leftFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 120 : 120);
                        } else {
                            myTileToPlay = null;
                        }

                        // If we found a face match, let's give it a try and place it.
                        if (myTileToPlay != null) {

                            // Place the tile!
                            if (board.placeTile(myTileToPlay, row, col)) {

                                System.out.println(String.format("   Played tile '%s' at location (%d,%d).", myTileToPlay, row, col));

                                // Now that we've played it, remove it from the players tray
                                tray.remove(myTileToPlay);

                                // Return back the tile we played so we can us it for choosing faces next time
                                tileToMatch = myTileToPlay;
                                break;

                            } else {
                                myTileToPlay = null;
                            }
                        }
                    }
                } else {
                    bLeftFaceOpen = false ;
                }


                if ( ( myTileToPlay == null ) && bRightFaceOpen ) {

                    // We are going to place it to the right of the played tile
                    row = tileRow;
                    col = tileCol - 1;

                    // Ok, so we have a space to the right, let's get the right face for this played tile
                    Face rightFace = played.getRightFace();

                    // Does the left face match a face on any of our tile's faces?
                    for (Tile trayTile : tray) {

                        // Always compare with a known orientation to make things simpler
                        trayTile.setOrientation(Orientation.UP);

                        // Assume we are going to play this one
                        myTileToPlay = trayTile;

                        // Compare all the faces
                        if (trayTile.getLeftFace().match(rightFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 120 : 0);
                        } else if (trayTile.getRightFace().match(rightFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 0 : 240);
                        } else if (trayTile.getMiddleFace().match(rightFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 240 : 120);
                        }

                        // Place the tile!
                        if (board.placeTile(myTileToPlay, row, col)) {

                            System.out.println(String.format("   Played tile '%s' at location (%d,%d).", myTileToPlay, row, col));

                            // Now that we've played it, remove it from the players tray
                            tray.remove(myTileToPlay);

                            // Return back the tile we played so we can us it for choosing faces next time
                            tileToMatch = played;

                            // Show the full board.
                            System.out.println(board.display(false));
                            break ;

                        } else {
                            myTileToPlay = null;
                        }
                    }

                    // If we are here, that means that we didn't find a left or right spot.  So, now we need to
                    //   look above and below to see if a piece fits there...
                }

                if ( ( myTileToPlay == null ) && bMiddleFaceOpen ) {

                    row = tileRow + directionToLook;
                    col = tileCol;

                    // Ok, so we have a space below, let's get the correct face for this played tile
                    Face middleFace = played.getMiddleFace();
                    myTileToPlay = null;

                    // Does the left face match a face on any of our tile's faces?
                    for (Tile trayTile : tray) {

                        // Always compare with a known orientation to make things simpler
                        trayTile.setOrientation(Orientation.UP);

                        // Assume we are going to play this one
                        myTileToPlay = trayTile ;

                        // Compare all the faces
                        if (trayTile.getLeftFace().match(middleFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 240 : 120);
                        } else if (trayTile.getRightFace().match(middleFace)) {
                            trayTile.setRotation((played.getOrientation() == Orientation.UP) ? 120 : 120);
                        } else if (trayTile.getMiddleFace().match(middleFace)) {
                            trayTile.setRotation(0);
                        }

                        // Place the tile!
                        if (board.placeTile(myTileToPlay, row, col)) {

                            System.out.println(String.format("   Played tile '%s' at location (%d,%d).", myTileToPlay, row, col));

                            // Now that we've played it, remove it from the players tray
                            tray.remove(myTileToPlay);

                            // Return back the tile we played so we can us it for choosing faces next time
                            tileToMatch = played;

                            // The tile's middle face is no longer free
                            bMiddleFaceOpen = false ;

                            break;

                        } else {
                            myTileToPlay = null ;
                        }
                    }
                }

                // If there are no more open faces, let's remove this tile from the list.
                if ( !bLeftFaceOpen && !bRightFaceOpen && !bMiddleFaceOpen ) {
                    iTile.remove();
                }

                // If we can't play a tile...tell them so...otherwise set the player on the tile
                if ( myTileToPlay == null ){
                    System.out.println("   Can't find a tile to play...");
                } else {
                    myTileToPlay.setPlayer(this);
                    break;
                }
            }
        }

        if ( myTileToPlay == null ) {

            StringBuilder strTray = new StringBuilder("    No matches: ");

            // Does the left face match a face on any of our tile's faces?
            for (Tile trayTile : tray) {
                strTray.append(trayTile).append(",");
            }

            System.out.println(strTray);

        } else {
            System.out.println(displayComparisonOfTwoTiles(tileToMatch,myTileToPlay));
        }

        return myTileToPlay;
    }
}