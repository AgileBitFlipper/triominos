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
            Tile highestTile = highestValueTile() ;
            if ( highestTile != null )
                highestTile.draw(true, tile_desc);
            else
                description.append("No more tiles in tray.\n");
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

        Tile choice ;

        // Is this the first tile played?
        if ( board.count() == 0 ) {

            choice = playFirstTile(board);

        } else {

            choice = null;

            // Let's see if we have any choices to play
            ArrayList<Tile> choicesToPlay = new ArrayList<Tile>();

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

                boolean bLeftFaceOpen   = bWeCanLookLeft && ( board.playedTiles[tileRow][tileCol - 1] == null ) ;
                boolean bRightFaceOpen  = bWeCanLookRight && ( tileCol > 0 ) && ( board.playedTiles[tileRow][tileCol + 1] == null ) ;
                boolean bMiddleFaceOpen = ( bWeCanLookDown || bWeCanLookUp ) && ( board.playedTiles[tileRow + directionToLook][tileCol] == null ) ;

                // Can we play one of our tiles to the left?
                if ( bLeftFaceOpen ) {
                    choice = getTileFromTrayForLeftFace(board, played.getLeftFace(), tileRow, tileCol - 1);
                    tray.remove(choice);
                }

                // Can we play one of our tiles to the right?
                if ( ( choice == null ) && bRightFaceOpen ) {
                    choice = getTileFromTrayForRightFace(board, played.getRightFace(), tileRow, tileCol + 1);

                    // Now that we've played it, remove it from the players tray
                    tray.remove(choice);
                }

                // Can we play one of our tiles up or down?
                if ( ( choice == null ) && bMiddleFaceOpen ) {
                    choice = getTileFromTrayForMiddleFace(board, played.getMiddleFace(), tileRow + directionToLook, tileCol);
                    tray.remove(choice);
                }

                // If there are no more open faces, let's remove this tile from the list.
                if ( !bLeftFaceOpen && !bRightFaceOpen && !bMiddleFaceOpen ) {
                    iTile.remove();
                }

                // If we can't play a tile...tell them so...otherwise set the player on the tile
                if ( choice == null ){
                    System.out.println("   Can't find a tile to play...");
                } else {
                    System.out.println(String.format("   Played tile '%s' at location (%d,%d).", choice, choice.getRow(), choice.getCol()));
                    choice.setPlayer(this);
                    break;
                }
            }
        }

        if ( choice == null ) {

            StringBuilder strTray = new StringBuilder("    No matches: ");

            // Does the left face match a face on any of our tile's faces?
            for (Tile trayTile : tray) {
                strTray.append(trayTile).append(",");
            }

            System.out.println(strTray);

        } else {

            // We found a tile to play, so let's add it to the list
            if ( tileHasAnEmptyFace(board, choice) ) {
                System.out.println("  Tile added to empty faces pool: " + choice );
                tilesWithAvailableFaces.add(choice);
            } else {
                System.out.println("  Tile removed from empty faces pool: " + choice );
                tilesWithAvailableFaces.remove(choice);
            }
        }

        return choice;
    }

    private Tile getTileFromTrayForMiddleFace(Board board, Face middleFace, int row, int col) {
        Tile choice = null;
        Orientation orientation = board.getOrientationForPositionOnBoard(row+1,col);

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : tray) {

            // Always compare with a known orientation to make things simpler
            trayTile.setOrientation(Orientation.UP);

            // Assume we are going to play this one
            choice = trayTile ;

            // Compare all the faces
            if (trayTile.getLeftFace().match(middleFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 240 : 120);
            } else if (trayTile.getRightFace().match(middleFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 120 : 120);
            } else if (trayTile.getMiddleFace().match(middleFace)) {
                trayTile.setRotation(0);
            }

            // Place the tile!
            if (board.placeTile(choice, row, col)) {
                break;
            } else {
                choice = null ;
            }
        }
        return choice;
    }

    private Tile getTileFromTrayForRightFace(Board board, Face rightFace, int row, int col) {

        Tile choice = null;
        Orientation orientation = board.getOrientationForPositionOnBoard(row,col-1);

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : tray) {

            // Always compare with a known orientation to make things simpler
            trayTile.setOrientation(Orientation.UP);

            // Assume we are going to play this one
            choice = trayTile;

            // Compare all the faces
            if (trayTile.getLeftFace().match(rightFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 120 : 0);
            } else if (trayTile.getRightFace().match(rightFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 0 : 240);
            } else if (trayTile.getMiddleFace().match(rightFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 240 : 120);
            }

            // Place the tile!
            if (board.placeTile(choice, row, col)) {

                // Show the full board.
                System.out.println(board.display(false));
                break ;

            } else {
                choice = null;
            }
        }

        // If we are here, that means that we didn't find a left or right spot.  So, now we need to
        //   look above and below to see if a piece fits there...
        return choice;
    }

    private Tile getTileFromTrayForLeftFace(Board board, Face leftFace, int row, int col) {
        Tile choice = null;
        Orientation orientation = board.getOrientationForPositionOnBoard(row,col+11);

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : tray) {

            // Always compare with a known orientation to make things simpler
            trayTile.setOrientation(Orientation.UP);

            // Assume we are going to play this one
            choice = trayTile;

            // Compare all the faces
            if (trayTile.getLeftFace().match(leftFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 120 : 240);
            } else if (trayTile.getRightFace().match(leftFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 240 : 0);
            } else if (trayTile.getMiddleFace().match(leftFace)) {
                trayTile.setRotation((orientation == Orientation.UP) ? 120 : 120);
            } else {
                choice = null;
            }

            // If we found a face match, let's give it a try and place it.
            if (choice != null) {

                // Place the tile!
                //@Todo: This only allows for a single choice for a position when there could be more
                //@Todo: Perhaps we could return an ArrayList of choices for this face?
                if (board.placeTile(choice, row, col)) {
                    break;
                } else {
                    choice = null;
                }
            }
        }
        return choice;
    }

    private boolean tileHasAnEmptyFace(Board board, Tile played) {

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

        return ( bLeftFaceOpen || bRightFaceOpen || bMiddleFaceOpen ) ;
    }
}