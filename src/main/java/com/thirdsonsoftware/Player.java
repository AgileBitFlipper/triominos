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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

enum Mode {
    RELEASE,
    DEBUG
}

/**
 * A player for the game of triominos
 */
public class Player implements Serializable {

    private Mode mode ;               // Playing around with the idea of an output mode

    private int wonAGameCount ;       // How many times has this player won a game
    private int score ;               // The players score
    private boolean starts;           // This player starts the game (highest tile)
    private String name ;             // The name of the player
    private Tile startingTile ;       // Get the starting tile

    final ArrayList<Tile> tray;       // The tray in the player's hand

    /**
     * @param name - name of player
     */
    public Player(String name) {
        setWonAGameCount(0);
        setScore(0);
        setStarts(false);
        setName(name);
        tray = new ArrayList<>(56);
        setMode(Mode.RELEASE);
        setStarts(false);
    }

    /**
     * How many games has this player won?
     * @param count
     */
    public void setWonAGameCount(int count) { wonAGameCount = count ; }
    public int getWonAGameCount() { return wonAGameCount ; }

    /**
     * For now, we use this to determine if we are in debug output mode or not
     * @param m (Mode) The mode to set
     */
    public void setMode( Mode m ) { mode = m; }
    public Mode getMode() { return mode; }

    /**
     * Retrieve the Player's score
     * @return - (int) score
     */
    public int getScore() {
        return score;
    }

    /**
     * Set the Player's score to the provided value
     * @param score - (int) The value to set the score to
     */
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

    public Tile getStartingTile() {
        return startingTile;
    }

    public void setStartingTile(Tile startingTile) {
        this.startingTile = startingTile;
    }

    public ArrayList<Tile> getTray() {
        return tray;
    }

    /**
     * Pop the top tile from the pool
     * Assumes the pool has been properly shuffled for
     *   randomness
     *
     * @param tilePool - the pool of tray to draw from
     */
    public boolean drawTile( ArrayList<Tile> tilePool, int round ) {
        Tile t = tilePool.remove(0);
        t.setPlayer(this);
        Event.logEvent(EventType.DRAW_A_TILE,t,this,round);
        Log.Info(String.format( "   Removing tile %s and adding it to %s's tray.", t, name ) );
        return getTray().add(t);
    }

    /**
     * @return the highest valued tile in this players tray
     */
    public Tile highestValueTile() {

        Tile highestValue = null ;

        if ( getTray().size() > 0 ) {
            for ( Tile t : getTray()) {
                if ( ( highestValue == null ) || ( t.getValue() > highestValue.getValue() ) ) {
                    highestValue = t;
                }
            }
        }
        return highestValue ;
    }

    /**
     * The highest value tile is not necessarily the first tile to play.
     *   The reason is based on bonuses.
     * @return (Tile) The tile that provides for the most points (value + bonus)
     */
    public Tile determineFirstTile() {
        Tile myTileToPlay;
        if ( hasZeroTriplet() ) {
            myTileToPlay = getZeroTriplet();
        } else if ( hasTriplet() ) {
            myTileToPlay = getLargestTriplet();
        } else {
            myTileToPlay = highestValueTile();
        }
        setStartingTile(myTileToPlay);
        return myTileToPlay;
    }

    /**
     * This method is only called when the first tile is played.  The rules about
     *   starting and choosing the first tile are a bit complex, and are score based.
     * @param board - the board we are putting our pieces on
     * @return (Choice) - the tile chosen to play after it was placed
     */
    private Choice playFirstTile( Board board ) {

        // Always start in the middle of the board.
        int row = 56 ;
        int col = 56 ;

        Tile myTileToPlay = determineFirstTile() ;

        Choice myChoice = new Choice( myTileToPlay,56,56, board.getOrientationForPositionOnBoard(row,col),0) ;

        // Let's let everyone know the type of tile this choice is...
        if ( myTileToPlay.getValue() == 0 ) {
            Event.logEvent(EventType.TRIPLE_ZERO_BONUS, board.getRound(), myTileToPlay);
            Log.Info("   Playing zero-triplet tile for 30 point bonus!");
        } else if ( myTileToPlay.isTriplet() ) {
            Event.logEvent(EventType.TRIPLE_PLAY_BONUS, board.getRound(), myTileToPlay);
            Log.Info("   Playing highest value triplet tile for 10 point bonus!");
        } else {
            Event.logEvent(EventType.HIGHEST_TILE_START, board.getRound(), myTileToPlay);
            Log.Info("   Playing highest value tile for no bonus!");
        }

        // Return back the tile we played so we can us it for choosing faces next time
        return myChoice ;
    }

    /**
     * @param board the board that a tile is placed on
     */
    public Tile playATile(Board board, ArrayList<Tile> playedTiles, ArrayList<Tile> tilesWithAvailableFaces ) {

        // Let's see if we have any choices to play
        ArrayList<Choice> choicesToPlay = new ArrayList<Choice>();

        // If this is our first piece, add the first tile
        if ( board.count() == 0 ) {
            choicesToPlay.add(playFirstTile(board));
        }

        Iterator<Tile> iTile = tilesWithAvailableFaces.iterator();

        // Let's look at our played tiles and see if any have available faces...
        //   Add all choices to the list so we can find the most valuable
        while (iTile.hasNext()) {

            Tile played = iTile.next();

            if (getMode() == Mode.DEBUG)
                Log.Debug("Tile to match:\n" + showTile(played));

            int tileRow = played.getRow();
            int tileCol = played.getCol();

            Orientation playedTileOrientation = played.getOrientation();
            int directionToLook = (playedTileOrientation == Orientation.UP) ? 1 : -1;

            boolean bWeCanLookLeft = (tileCol > 0);
            boolean bWeCanLookRight = (tileCol < board.getNumberOfCols() - 1);
            boolean bWeCanLookDown = ((directionToLook > 0) && (tileRow < board.getNumberOfRows() - 1));
            boolean bWeCanLookUp = ((directionToLook < 0) && (tileRow > 0));

            boolean bLeftFaceOpen = bWeCanLookLeft && (board.playedTiles[tileRow][tileCol - 1] == null);
            boolean bRightFaceOpen = bWeCanLookRight && (tileCol > 0) && (board.playedTiles[tileRow][tileCol + 1] == null);
            boolean bMiddleFaceOpen = (bWeCanLookDown || bWeCanLookUp) && (board.playedTiles[tileRow + directionToLook][tileCol] == null);

            ArrayList<Choice> choicesForAFace;

            // Can we play any of our tiles to the left?
            if (bLeftFaceOpen) {
                choicesForAFace = getTileFromTrayForLeftFace(played, played.getLeftFace(), tileRow, tileCol - 1);
                choicesToPlay.addAll(choicesForAFace);
            }

            // Can we play any of our tiles to the right?
            if (bRightFaceOpen) {
                choicesForAFace = getTileFromTrayForRightFace(played, played.getRightFace(), tileRow, tileCol + 1);
                choicesToPlay.addAll(choicesForAFace);
            }

            // Can we play any of our tiles up or down?
            if (bMiddleFaceOpen) {
                choicesForAFace = getTileFromTrayForMiddleFace(played, played.getMiddleFace(), tileRow + directionToLook, tileCol);
                choicesToPlay.addAll(choicesForAFace);
            }

            // If there are no more open faces, let's remove this tile from the list.
            if ( !bLeftFaceOpen && !bRightFaceOpen && !bMiddleFaceOpen ) {
                iTile.remove();
            }
        }

        int highestScore = -1 ;    // 0 is a valid score for tile '0-0-0'
        Choice topChoice = null ;
        Tile tileToPlay = null ;

        // Spin through choices looking for the highest value or score
        for ( Choice c : choicesToPlay ) {

            c.setTestForFitOnly(true);

            // Test to see if the choice fits or not before deciding if it's worth it.
            if ( board.pieceFits( c ) ) {

                // Get value for a tile needs to include bonus scoring...
                if (c.getScore() > highestScore) {
                    highestScore = c.getScore();
                    topChoice = c;
                }
            }
        }

        if ( getMode() == Mode.DEBUG )
            displayChoices("  Choices:",choicesToPlay);

        // Do we have a top choice from the list of choices?
        if ( topChoice != null ) {

            int row   = topChoice.getRow() ;
            int col   = topChoice.getCol() ;

            tileToPlay = topChoice.getTile();

            tileToPlay.setOrientation(topChoice.getOrientation());
            tileToPlay.setRotation(topChoice.getRotation());
            tileToPlay.setPlayer(this);

            topChoice.setTestForFitOnly(false);

            Log.Debug("  Top choice: " + topChoice.getTile());

            if ( board.placeTile(topChoice)) {

                Event.logEvent(EventType.PLACE_A_TILE, board.getRound(), topChoice.getTile());

                this.setScore(this.getScore()+topChoice.getScore());

                getTray().remove(tileToPlay);
                Log.Debug(board.display(false));
                Log.Info(String.format("   Played tile '%s' at location (%d,%d).", tileToPlay, tileToPlay.getRow(), tileToPlay.getCol()));

                // We found a tile to play, so let's add it to the list
                if ( tileHasAnEmptyFace(board, tileToPlay) ) {
                    Log.Debug("  Tile added to empty faces pool: " + tileToPlay );
                    tilesWithAvailableFaces.add(tileToPlay);
                } else {
                    Log.Debug("  Tile removed from empty faces pool: " + tileToPlay );
                    tilesWithAvailableFaces.remove(tileToPlay);
                }

            } else {
                // We can't place it, so let's not pretend we can!
                tileToPlay = null ;
                Log.Info(String.format("--- Unable to place tile '%s' on board @ (%d,%d) with o:%s r:%d ---", tileToPlay, row, col, tileToPlay.getOrientation(), tileToPlay.getRow() ) );
            }

        }

        // Ultimately, did we play a tile?
        if ( tileToPlay == null ) {

            Log.Debug(String.format("--- Player '%s' can't find a tile to play ---", this.getName()));

            StringBuilder strTray = new StringBuilder("    No matches: ");

            // Does the left face match a face on any of our tile's faces?
            for (Tile trayTile : getTray()) {
                strTray.append(trayTile).append(",");
            }

            Log.Debug(strTray.toString());

        }

        return tileToPlay;
    }

    /**
     * Let's move through the tiles from our tray and see if any match the middle face
     *   of the tile played.
     *
     *  ------------------
     *           56            'A' is the tile played on the board
     *  ------------------     'B' is one possible match in our tray (2-4-4)
     *  |    |++++^
     *  |    |+++/2\           'B' only matches to the middle if we orient up
     *  | 55 |++/ B \             and rotate 0 degrees.
     *  |    |+/4   4\
     *  |    | -------
     *  |    | -------
     *  |    |+\4   4/
     *  | 56 |++\ A /
     *  |    |+++\4/
     *  |    |++++v
     *
     * @param played - the tile played that we are trying to match
     * @param middleFace - the middle face we are trying to match
     * @param row - the row where the tile is played
     * @param col - the column where the tile is played
     * @return (ArrayList<Choice>) the list of possible choices to be played
     */
    private ArrayList<Choice>  getTileFromTrayForMiddleFace(Tile played, Face middleFace, int row, int col) {

        ArrayList<Choice> choices = new ArrayList<>();
        boolean aMatchWasFound ;

        Orientation orientationOfTileToMatch = played.getOrientation();
        Orientation orientationOfTrayTile = (orientationOfTileToMatch==Orientation.UP) ? Orientation.DOWN : Orientation.UP;

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : getTray()) {

            // Always compare with a known orientation to make things simpler
            trayTile.setOrientation(orientationOfTrayTile);
            trayTile.setRotation(0);

            // Assume we are going to play this one
            aMatchWasFound = true ;

            // Compare all the faces
            if (trayTile.getLeftFace().match(middleFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 240 : 120);
            } else if (trayTile.getRightFace().match(middleFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 120 : 240);
            } else if (trayTile.getMiddleFace().match(middleFace)) {
                trayTile.setRotation(0);
            } else {
                aMatchWasFound = false ;
            }

            // Assume we are going to play this one
            if ( aMatchWasFound ) {
                if ( getMode() == Mode.DEBUG ) {
                    if (orientationOfTileToMatch == Orientation.UP) {
                        Log.Info("== Match Face Below ==");
                        Log.Info(showTwoTilesTopAndBottom(played, trayTile));
                    } else {
                        Log.Info("== Match Face Above ==");
                        Log.Info(showTwoTilesTopAndBottom(trayTile, played));
                    }
                }
                choices.add(new Choice( trayTile, row, col, trayTile.getOrientation(), trayTile.getRotation()));
            }
        }
        return choices;
    }

    /**
     * Let's move through the tiles from our tray and see if any match the right face
     *   of the tile played.
     *
     *  -------------------
     *            56   57       'A' is the tile played on the board
     *  -------------------     'B' is one possible match in our tray (2-4-4)
     *  |    |+ ------- ^
     *  |    |++\4   4//2\      'B' only matches to the left if we orient up
     *  | 56 |+++\ A // B \         and rotate 120 degrees.
     *  |    |++++\4//4   4\
     *  |    |++++ v -------
     *
     * @param played - the tile played that we are trying to match
     * @param rightFace - the right face we are trying to match
     * @param row - the row where the tile is played
     * @param col - the column where the tile is played
     * @return (ArrayList<Choice>) the list of possible choices to be played
     */
    private ArrayList<Choice>  getTileFromTrayForRightFace(Tile played, Face rightFace, int row, int col) {

        ArrayList<Choice> choices = new ArrayList<>();
        boolean aMatchWasFound ;

        Orientation orientationOfTileToMatch = played.getOrientation();
        Orientation orientationOfTrayTile = (orientationOfTileToMatch==Orientation.UP) ? Orientation.DOWN : Orientation.UP;

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : getTray()) {

            // Always compare with a known orientation to make things simpler
            trayTile.setOrientation(orientationOfTrayTile);
            trayTile.setRotation(0);

            aMatchWasFound = true ;

            // Compare all the faces
            if (trayTile.getLeftFace().match(rightFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 0 : 0);
            } else if (trayTile.getRightFace().match(rightFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 240 : 120);
            } else if (trayTile.getMiddleFace().match(rightFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 120 : 240);
            } else {
                aMatchWasFound = false ;
            }

            if ( aMatchWasFound ) {
                if ( getMode() == Mode.DEBUG ) {
                    Log.Info("== Match Right Face ==");
                    Log.Info(showTwoTilesLeftAndRight(played, trayTile));
                }
                choices.add(new Choice( trayTile, row, col, trayTile.getOrientation(), trayTile.getRotation()));
            }
        }
        return choices;
    }

    /**
     * Let's move through the tiles from our tray and see if any match the left face
     *   of the tile played.
     *
     *  ------------------------
     *            55   56            'A' is the tile played on the board
     *  ------------------------     'B' is one possible match in our tray (2-4-4)
     *  |    |+++++^ -------
     *  |    |++++/4\\4   4/         'B' only matches to the left if we orient up
     *  | 56 |+++/ B \\ A /              and rotate 240 degrees.
     *  |    |++/2   4\\4/
     *  |    |+ ------- v
     *
     * @param played - the tile played that we are trying to match
     * @param leftFace - the left face we are trying to match
     * @param row - the row where the tile is played
     * @param col - the column where the tile is played
     * @return (ArrayList<Choice>) the list of possible choices to be played
     */
    private ArrayList<Choice> getTileFromTrayForLeftFace(Tile played, Face leftFace, int row, int col) {

        ArrayList<Choice> choices = new ArrayList<>();
        boolean aMatchWasFound ;

        Orientation orientationOfTileToMatch = played.getOrientation();
        Orientation orientationOfTrayTile    = (orientationOfTileToMatch==Orientation.UP) ? Orientation.DOWN : Orientation.UP;

        // Does the left face match a face on any of our tile's faces?
        for (Tile trayTile : getTray()) {

            // Make sure our tile in our tray is properly oriented to match
            trayTile.setOrientation(orientationOfTrayTile);
            trayTile.setRotation(0);

            aMatchWasFound = true ;

            // Compare all the faces
            if (trayTile.getLeftFace().match(leftFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 120 : 240);
            } else if (trayTile.getRightFace().match(leftFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 0 : 0);
            } else if (trayTile.getMiddleFace().match(leftFace)) {
                trayTile.setRotation((orientationOfTrayTile == Orientation.UP) ? 240 : 120);
            } else {
                aMatchWasFound = false ;
            }

            // Assume we are going to play this one
            if ( aMatchWasFound ) {
                if ( getMode() == Mode.DEBUG ) {
                    Log.Info("== Match Left Face ==");
                    Log.Info(showTwoTilesLeftAndRight(trayTile, played));
                }
                choices.add(new Choice( trayTile, row, col, trayTile.getOrientation(), trayTile.getRotation()));
            }

        }
        return choices;
    }

    /**
     * Does the tile played have an empty face?
     * @param board - the board that the tile is played on
     * @param played - the tile that has been played
     * @return (boolean) True if the tile has an empty face, False otherwise
     */
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

    /**
     * Does this player have a triplet in their tray?
     * @return (boolean) True if the player has a triplet, False otherwise.
     */
    private boolean hasTriplet() {
        boolean hasTriplet = false ;
        for( Tile tile : getTray() ) {
            if (tile.getValue() == tile.getCornerA() * 3) {
                hasTriplet = true;
                break;
            }
        }
        return hasTriplet ;
    }

    /**
     * Retrieves the largest triplet from the player's tray
     * @return (Tile) Largest triplet in the tray, null if there isn't one.
     */
    public Tile getLargestTriplet() {
        Tile largestTriplet = null;

        for ( Tile tile : getTray() ) {
            if ( tile.isTriplet() &&
                    ( ( largestTriplet == null ) ||
                    ( tile.getValue() > largestTriplet.getValue() ) ) ) {
                largestTriplet = tile ;
            }
        }
        return largestTriplet;
    }

    /**
     * Retrieves the largest value tile from the Player's tray
     * @return (Tile) Largest value triplet in the tray, null if there isn't one.
     */
    public Tile getLargestValuedTile() {
        Tile largestValueTile = null;

        for ( Tile tile : getTray() ) {
            if ( ( largestValueTile == null ) ||
                    ( tile.getValue() > largestValueTile.getValue() ) ) {
                largestValueTile = tile ;
            }
        }
        return largestValueTile;
    }

    /**
     * Does this player have a zero-triplet tile?
     * @return True if this Player has a zero-triplet tile, False otherwise.
     */
    private boolean hasZeroTriplet() {
        boolean haveIt = false ;
        for ( Tile tile : getTray() ) {
            if ( tile.getValue() == 0 ) {
                haveIt = true ;
                break;
            }
        }
        return haveIt;
    }

    /**
     * Retrieves the zero-triplet file from the Player's tray
     * @return (Tile) The zero-triplet tile if there is one, null otherwise.
     */
    private Tile getZeroTriplet() {
        Tile zeroTriplet = null;
        for ( Tile tile : getTray() ) {
            if ( tile.getValue() == 0 )
                zeroTriplet = tile ;
        }
        return zeroTriplet;
    }

    /**
     * Builds a String that compares the two provided tiles
     * @param a - the tile to show on the left
     * @param b - the tile to show on the right
     * @return (String) The string comparing the two tiles
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
                lines[4] += "    - ";
            } else {
                lines[4] += " - ";
                lines[3] += " - ";
                lines[2] += "  - ";
                lines[1] += "   - ";
                lines[0] += "    - ";
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
     * Builds a String that describes the list of tiles with a title
     * @param name - the title to display with the list
     * @param list - the list of 'choice' tiles to display
     * @return (String) the string describing the tile choices
     */
    private String displayChoices(String name, ArrayList<Choice> list) {
        StringBuilder strTiles = new StringBuilder();
        strTiles.append(String.format("%s (%d):\n  [", name, list.size()));
        if ( !list.isEmpty() ) {
            for (Choice choice : list) {
                Tile tile = choice.getTile();
                if (list.lastIndexOf(choice) != list.size() - 1)
                    strTiles.append(tile).append(", ");
                else
                    strTiles.append(tile);
            }
        } else {
            strTiles.append("<empty>");
        }
        strTiles.append("]\n");
        return strTiles.toString();
    }

    /**
     * Constructs a String that represents the list of tiles in either number
     *   or tile form.
     * @param asTile - True if the string should show Tile representation, values otherwise
     * @param name - The name to show with the list
     * @param list - The list of tiles to display
     * @return (String) the string representing the list of tiles
     */
    static protected String displayTiles(Boolean asTile, String name, ArrayList<Tile> list) {
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
                    rows[i] = "      ";
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
     * Adds the given tile to the rows of Strings provided, and drawing
     *   it based on if the tile is going to be displayed by itself or not.
     * @param rows - the array of Strings in which to draw the tile
     * @param tile - the tile to draw
     * @param solo - if the tile is by itself or not
     */
    static public void addTile(String[] rows, Tile tile, boolean solo) {
        tile.draw(solo,rows);
    }

    /**
     * Builds a single string that represents the tile given.
     * @param tile - the tile to display in String form
     * @return (String) - the display string for the tile
     */
    static public String showTile(Tile tile) {
        StringBuilder strReturn = new StringBuilder(50);
        String[] rows = new String[5];
        for (int i=0;i<5;i++)
            rows[i]="";
        addTile(rows,tile,true);
        for ( String strRow : rows )
            strReturn.append(strRow).append("\n");
        return strReturn.toString();
    }

    /**
     * Builds a single string that depicts two tiles, one left and one right.
     * @param tileLeft - the Tile to display to the left
     * @param tileRight - the Tile to display to the right
     * @return (String) the String representing the two tiles sitting side by side
     */
    static public String showTwoTilesLeftAndRight(Tile tileLeft, Tile tileRight) {
        StringBuilder strReturn = new StringBuilder(50);
        String[] rows = new String[5];
        for (int i=0;i<5;i++)
            rows[i]="";
        addTile(rows,tileLeft,true);
        addTile(rows,tileRight, true);
        for ( String strRow : rows )
            strReturn.append(strRow).append("\n");

        return strReturn.toString();
    }

    /**
     * Builds a single string that depicts two tiles, one above and one below.
     * @param top - the Tile to display on top
     * @param bottom - the Tile to display on bottom
     * @return (String) the String representing the two tiles sitting one on top of the other.
     */
    static public String showTwoTilesTopAndBottom(Tile top, Tile bottom) {
        StringBuilder strReturn = new StringBuilder(50);
        String[] rowsTop = new String[5];
        for (int i=0;i<5;i++)
            rowsTop[i]="";
        String[] rowsBottom = new String[5];
        for (int i=0;i<5;i++)
            rowsBottom[i]="";
        addTile(rowsTop,top,true);
        addTile(rowsBottom,bottom,true);
        for ( String strRow : rowsTop )
            strReturn.append(strRow).append("\n");
        for ( String strRow : rowsBottom )
            strReturn.append(strRow).append("\n");
        return strReturn.toString();
    }

    /**
     * @return string that represents the Player's state
     */
    public String toString() {
        String description = String.format("  Name: %s\n    Starts: %s\n    Score: %d\n%s",
                name,
                starts ? "yes" : "no",
                score,
                displayTiles(true, "    Hand", getTray()));
        return description;
    }

    /**
     * We need this for HashMap to work correctly for a Player.
     * The name is the only really relevant field for a player of a game.
     * @return hash of the Player's name
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * We need this for HashMap to work when using a Player as a key
     * @param o - Object to compare this against
     * @return true if names are the same, false otherwise
     */
    public boolean equals(Object o) {
        if ((o instanceof Player) && ((Player) o).getName().equals(name))
            return true;
        return false;
    }

}