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
import java.util.Iterator;

/**
 *
 * This is a smaller example of a game board.  The game board, by default, is 112 by 112
 *  allowing for free flow in all directions from the center starting point of 56,56. All
 *  references to coordinates are Y-axis first, followed by X-axis (row,col) format.  This
 *  is due to the nature of our choice of frame of reference.
 *  
 * ===================== Game Board ======================...=========
 * |                                                         1 1 1 1 |
 * |                        1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 ...0 0 1 1 |
 * |      0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 ...8 9 0 1 |
 * =======================================================...=========
 * |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /... /\  /\ |
 * | 0 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ .../  \/  \|
 * |   |--------------------------------------------------...--------|
 * |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ ...\  /\  /|
 * | 1 | \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \... \/  \/ |
 * |   |--------------------------------------------------...--------|
 * |   | /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /... /\  /\ |
 * | 2 |/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/ .../  \/  \|
 * |   |--------------------------------------------------...--------|
 * |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ ...\  /\  /|
 * ...................................................................
 * ...................................................................
 * ...................................................................
 * |   |--------------------------------------------------...--------|
 * |   |\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\  /\ ...\  /\  /|
 * |111| \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \/  \... \/  \/ |
 * =======================================================...=========
 * 
 * Row + Col == Even means tile points up
 * Row + Col == Odd  means tile points down
 *
 */
public class Board implements Serializable {

    protected boolean bUseColor = false ;

    private String  c = Log.CYAN,
                    p = Log.PURPLE,
                    r = Log.RESET;

    protected static final int BRIDGE_BONUS  = 40 ;
    protected static final int HEXAGON_BONUS = 50 ;

    protected static final int DEFAULT_ROWS = 112 ;
    protected static final int DEFAULT_COLS = 112 ;

    private int num_rows ;
    private int num_cols ;

    private int topBorder    = DEFAULT_ROWS ;
    private int bottomBorder ;
    private int leftBorder   = DEFAULT_COLS;
    private int rightBorder  ;

    final Tile[][] playedTiles ;

    /**
     * Build the game board with the default size
     */
    public Board() {
        playedTiles = new Tile[DEFAULT_ROWS][DEFAULT_COLS];
        num_cols=DEFAULT_COLS;
        num_rows=DEFAULT_ROWS;
        setUseColor(false);
        clearBoard();
    }

    /**
     * Build the game board with the specified rows and columns
     * @param rows
     * @param cols
     */
    public Board( int rows, int cols ) {
        playedTiles = new Tile[rows][cols] ;
        num_rows=rows;
        num_cols=cols;
        setUseColor(false);
        clearBoard();
    }

    /**
     * Tell the board to use color or not.  Some terminals don't
     *   support ANSI colors, so this is a way to turn it off if
     *   needed.  In the case of testing, color is turned off.
     * @param useColor
     */
    protected void setUseColor(boolean useColor) {
        bUseColor = useColor;
        Tile.setUseColor(useColor);
        if ( bUseColor ) {
            c = Log.CYAN ;
            p = Log.PURPLE ;
            r = Log.RESET ;
        } else {
            c = p = r = "";
        }
    }

    /**
     * Overload for findBoardMinMax(boolean)
     */
    protected void findBoardMinMax() {
        findBoardMinMax(false);
    }

    /**
     * Determines the true size of the board based on the provided input choice.
     *   If 'full' is set to true, the method is used to set the border values to
     *   the existing values specified in the rows and columns found .
     * If the value of 'full' is set to false, it will look for the min and max
     *   values and display the smallest version of the board that shows all tiles.
     * @param full - (boolean) If true, set border values to the current rows and cols values.
     *             If false, set borders based on the placement of the tiles.
     */
    protected void findBoardMinMax(boolean full ) {

        // If we weren't asked to display the full board, let's find ou
        //   where the left, top, right, and bottom borders are.
        if ( !full ) {

            // Max out the minimum values, and minimize out the maximum values.
            topBorder = num_rows ;
            leftBorder = num_cols ;
            bottomBorder = rightBorder = 0 ;

            // Now, spin through the board and find the rows and cols
            //   closest to the edges
            for (int row = 0; row < num_rows; row++) {

                for (int col = 0; col < num_cols; col++) {

                    // if the spot on the board has a tile, set the
                    //   values as appropriate.
                    if ( playedTiles[row][col] != null ) {

                        // Set the top
                        if ( row < topBorder)
                            topBorder = row ;

                        // Set the bottom
                        if ( row > bottomBorder)
                            bottomBorder = row ;

                        // Set the left
                        if ( col < leftBorder)
                            leftBorder = col ;

                        // set the right
                        if ( col > rightBorder)
                            rightBorder = col ;
                    }
                }
            }

        } else {

            // The user asked for a full board display
            topBorder = leftBorder = 0 ;
            bottomBorder = num_rows ;
            rightBorder = num_cols ;
        }
    }

    /**
     * @return (int) number of rows in the board
     */
    public int getNumberOfRows() {
        return num_rows;
    }

    /**
     * @param num_rows (int) Sets the number of rows in the board
     */
    public void setNumberOfRows(int num_rows) {
        this.num_rows = num_rows;
    }

    /**
     * @return (int) number of columns in the board
     */
    public int getNumberOfCols() {
        return num_cols;
    }

    /**
     * @param num_cols (int) set the number of columns in the board
     */
    public void setNumberOfCols(int num_cols) {
        this.num_cols = num_cols;
    }

    /**
     * @return (int) current value of the topBorder (the top edge of the
     *   piece places closest to the top border for display purposes)
     */
    public int getTopBorder() { return topBorder; }

    /**
     * @param topBorder (int) Sets the topBoarder value
     */
    public void setTopBorder(int topBorder) {
        if ( topBorder < 0 ) {
            setTopBorder(0);
        } else if ( topBorder >= getNumberOfRows() ) {
            setTopBorder(getNumberOfRows() - 1);
        } else {
            this.topBorder = topBorder;
        }
    }

    /**
     * @return (int) current value of the bottomBorder (the bottom edge of the
     *   piece places closest to the bottom border for display purposes)
     */
    public int getBottomBorder() {
        return bottomBorder;
    }

    /**
     * @param bottomBorder (int) sets the value of the bottom border
     */
    public void setBottomBorder(int bottomBorder) {
        if ( bottomBorder < 0 ) {
            setBottomBorder(0);
        } else if ( bottomBorder >= getNumberOfRows() ) {
            setBottomBorder(getNumberOfRows() - 1);
        } else {
            this.bottomBorder = bottomBorder;
        }
    }

    /**
     * @return (int) the left edge of all pieces played on the board
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * @param leftBorder (int) the left edge of all pieces that have been played
     *                   on the board.
     */
    public void setLeftBorder(int leftBorder) {
        if ( leftBorder < 0 ) {
            setBottomBorder(0);
        } else if ( leftBorder >= getNumberOfCols() ) {
            setLeftBorder(getNumberOfCols()-1);
        } else {
            this.leftBorder = leftBorder;
        }
    }

    /**
     * @return (int) the right edge of all pieces played on the board
     */
    public int getRightBorder() {
        return rightBorder;
    }

    /**
     * @param rightBorder (int) sets the right edge of all pieces played on the board
     */
    public void setRightBorder(int rightBorder) {
        if ( rightBorder < 0 ) {
            setRightBorder(0);
        } else if ( rightBorder >= getNumberOfCols() ) {
            setRightBorder(getNumberOfCols()-1);
        } else {
            this.rightBorder = rightBorder;
        }
    }

    /**
     * Simply determines the Orientation of a tile when placed on the board
     *  based on the row and the column that it is placed.  If (row + col) is
     *  an even value, the tile is Oriented upwards (Orientation.UP), if the
     *  value is odd, the tile is Oriented downwards (Oriented.DOWN).
     * @param row - the row for the tile
     * @param col - the column for the tile
     * @return Orientation.UP or Orientation.DOWN
     */
    public Orientation getOrientationForPositionOnBoard(int row, int col) {
        return ( ( ( ( row + col ) % 2 ) == 0 ) ? Orientation.DOWN : Orientation.UP ) ;
    }

    /**
     * Clears the current board of all pieces.  This is a
     *   destructive move; no pieces are moved to any tray
     *   or pile.
     */
    protected void clearBoard() {
        for ( int x=0; x<getNumberOfRows(); x++)
            for ( int y=0; y<getNumberOfCols(); y++)
                playedTiles[x][y] = null ;
    }

    /**
     * Returns the number of tiles that have been played on
     *   the board.
     * @return count of tiles on the board (int)
     */
    public int count() {
        int count=0;
        for ( Tile row[]:playedTiles ) {
            for ( Tile tile:row ) {
                if ( tile != null )
                    count++;
            }
        }
        return count;
    }

    /**
     * @param row - The row of the played tile we are requesting
     * @param col - The column of the played tile we are requesting
     * @return - A Tile if one has been played there, null otherwise
     */
    public Tile pieceAtLocation(int row, int col) {
        return playedTiles[row][col];
    }

    /**
     * Verifies that the specified space on the board is unoccupied, and
     *   that all of the adjacent faces and corners will allow it to be
     *   played there.
     * @param t - the tile to determine if it fits
     * @param row - the row of the location to place the tile
     * @param col - the column of the location to place the tile
     * @return true if it fits, and false otherwise
     */
    public boolean pieceFits(Tile t, int row, int col, Choice choice) {

        boolean bItFits = false ;

        // We need to adjust the tile to the choice setting first...for now
        // Later, let's remove the use of the tile all together...
        t.setOrientation(choice.getOrientation());
        t.setRotation(choice.getRotation());

        // Is the slot empty?
        if ( playedTiles[row][col] == null ) {
            bItFits = leftFaceFits(t, row, col) &&
                    rightFaceFits(t, row, col) &&
                    middleFaceFits(t, row, col) &&
                    leftCornerFits(t, row, col) &&
                    middleCornerFits(t, row, col) &&
                    rightCornerFits(t, row, col);

            if (bItFits) {
                int score = calculateScore(t, row, col);
//                Log.Info(this.getClass().getName(),String.format("  Score for playing tile %s @ (%d,%d) is %s.", t, row, col, score));
                choice.setScore(score);
            }
        }
        return bItFits ;
    }

    /**
     * Determines if the left corner of this Tile fits on the board.
     * @param t - tile to be placed on the board
     * @param row - row in which to place it
     * @param col - column in which to place it
     * @return true if the tile's left corner matches all other tiles on the
     *         board or blank spaces
     */
    protected Boolean leftCornerFits(Tile t, int row, int col) {

        // This is a sample board, and we need to determine
        //   if a corner can fit or not.  In this case, we are
        //   looking at a bridge (row, column) @ (57,58).  Piece (29),
        //   '1-2-4' was placed as a bridge when it shouldn't have.
        //   The left corner needs to match with (row,col) @ (57,57),
        //   (57,56),(58,56),(58,57).  We can assume that the face
        //   test will handle any other match issues.
        // --------------------------------------------------------------
        //           52   53   54   55   56   57   58   59   60   61
        // --------------------------------------------------------------
        // |    |=+++++++++= -- 21-- ^ --  1-- ^ -- 19-- ^ -- 44-- ^
        // |    |==+++++++===\5 P 0//0\\0 P 0//0\\0 P 4//4\\4 P 2//2\
        // | 56 |===+++++=====\ B // B \\ A // A \\ B // A \\ B // A \
        // |    |====+++=======\5//5 P 0\\0//0 P 4\\4//4 P 4\\4//4 P 3\
        // |    |=====+=========v --  6-- v --  5-- v -- 53-- v -- 42--
        // |    |+++++=+++++++++^ -- 20-- ^+++++++++^ -- 34-- ^+++++++++
        // |    |++++===+++++++/5\\5 P 0//0\+++++++/4\\4 P 4//4\+++++++
        // | 57 |+++=====+++++/ A \\ B // B \+++++/ B \\ B // A \+++++
        // |    |++=======+++/4 P 4\\4//4 P 1\+++/2 P 1\\1//1 P 5\+++
        // |    |+=========+ -- 54-- v -- 10-- + -- 29-- v -- 35-- +
        // |    |=+++++++++=+++++++++^ -- 32-- =+++++++++=+++++++++=
        // |    |==+++++++===+++++++/4\\4 P 1/===+++++++===+++++++===
        // | 58 |===+++++=====+++++/ B \\ A /=====+++++=====+++++=====
        // |    |====+++=======+++/3 P 3\\3/=======+++=======+++=======
        // |    |=====+=========+ -- 48-- v=========+=========+=========

        // Assume it fits...
        boolean bItFits = true ;

        String whyItFails = "" ;

        boolean bWeCanLookLeft = ( col > 0 ) ;
        boolean bWeCanLookFarLeft = ( col > 1 ) ;
        boolean bWeCanLookDown = ( row < num_rows - 1 ) ;
        boolean bWeCanLookUp = ( row > 0 ) ;

        int cornerToMatch = t.getLeftCorner();

        // left
        if (bWeCanLookLeft) {
            Tile tileToTheLeft = pieceAtLocation(row,col-1);
            if ( ( tileToTheLeft != null) &&
                 ( tileToTheLeft.getMiddleCorner() != cornerToMatch ) ) {
                Log.Info(this.getClass().getName(),Player.showTwoTilesLeftAndRight(tileToTheLeft, t));
                bItFits = false;
                whyItFails = "left";
            }
        }
        // far-left
        if (bWeCanLookFarLeft) {
            Tile tileFarLeft = pieceAtLocation(row,col-2);
            if ( ( tileFarLeft != null) &&
                ( tileFarLeft.getRightCorner() != cornerToMatch ) ) {
                bItFits = false;
                whyItFails = "far-left";
            }
        }
        if ( t.getOrientation() == Orientation.UP ) {
            if (bWeCanLookDown) {
                // down && left
                if (bWeCanLookLeft) {
                    Tile tileDownLeft = pieceAtLocation(row+1,col-1);
                    if ( (tileDownLeft != null) &&
                            (tileDownLeft.getMiddleCorner() != cornerToMatch ) ) {
                        bItFits = false;
                        whyItFails = "down & left";
                    }
                }
                // down && far-left
                if (bWeCanLookFarLeft) {
                        Tile tileDownFarLeft = pieceAtLocation(row+1,col-2);
                        if ( (tileDownFarLeft != null) &&
                                (tileDownFarLeft.getRightCorner() != cornerToMatch ) ) {
                            bItFits = false;
                            whyItFails = "down & far-left";
                        }
                }
            }
        } else {
            // up && left
            if ((bWeCanLookUp) && (bWeCanLookLeft) &&
                    (pieceAtLocation(row - 1, col - 1) != null) &&
                    (pieceAtLocation(row - 1, col - 1).getMiddleCorner() != cornerToMatch)) {
                bItFits = false;
                whyItFails = "up & left";
            }
            // up && far-left
            if ((bWeCanLookUp) && (bWeCanLookFarLeft) &&
                    (pieceAtLocation(row - 1, col - 2) != null) &&
                    (pieceAtLocation(row - 1, col - 2).getRightCorner() != cornerToMatch)) {
                bItFits = false;
                whyItFails = "up & far-left";
            }
        }
        if (!bItFits)
            Log.Info(this.getClass().getName(), String.format("  Tile '%s' placement @ (%d,%d) fails left corner test - %s", t, row, col, whyItFails) ) ;
        return bItFits ;
    }

    /**
     * Determines if tile 't' can fit in location (row,col) based on the middle corner
     *  of the tile fitting with all other pieces on the board adjacent to the new tile
     *  if placed.
     * @param t - new tile to be placed
     * @param row - row for it to be placed in
     * @param col - col for it to be placed in
     * @return true if the tile's middle corner fits the location
     */
    protected boolean middleCornerFits(Tile t, int row, int col) {
        boolean bItFits = true ;

        String whyItFails = "" ;

        boolean bWeCanLookLeft = ( col > 0 ) ;
        boolean bWeCanLookRight = ( col < num_cols - 1 ) ;
        boolean bWeCanLookDown = ( row < num_rows - 1 ) ;
        boolean bWeCanLookUp = ( row > 0 ) ;

        if ( t.getOrientation() == Orientation.DOWN ) {

            if ( bWeCanLookDown ) {

                if ( bWeCanLookRight ) {

                    Tile tileDownAndRight = pieceAtLocation(row, col + 1);
                    if ((tileDownAndRight != null) &&
                            (tileDownAndRight.getLeftCorner() != t.getMiddleCorner())) {
                        Log.Info(this.getClass().getName(),Player.showTwoTilesLeftAndRight(t, pieceAtLocation(row, col + 1)));
                        bItFits = false;
                        whyItFails = "down & right";
                    }
                }

                Tile tileDown = pieceAtLocation(row + 1, col) ;
                if ( ( tileDown != null ) &&
                        ( tileDown.getMiddleCorner() != t.getMiddleCorner() ) ) {
                    bItFits = false;
                    whyItFails = "down";
                }

                if ( bWeCanLookLeft ) {
                    Tile tileDownAndLeft = pieceAtLocation(row + 1, col - 1) ;
                    if ( ( tileDownAndLeft != null ) &&
                        ( tileDownAndLeft.getRightCorner() != t.getMiddleCorner() ) ) {
                        bItFits = false;
                        whyItFails = "down & left";
                    }
                }
            }

        } else {

            if ( (bWeCanLookUp) && (bWeCanLookLeft) &&
                    (pieceAtLocation(row-1,col - 1) != null ) &&
                    (pieceAtLocation(row-1,col - 1).getRightCorner() != t.getMiddleCorner())) {
                bItFits = false;
                whyItFails = "up & left";
            }

            if ( (bWeCanLookUp) &&
                    (pieceAtLocation(row-1,col) != null ) &&
                    (pieceAtLocation(row-1,col).getMiddleCorner() != t.getMiddleCorner())) {
                bItFits = false ;
                whyItFails = "up";
            }

            if ( (bWeCanLookUp) && (bWeCanLookRight) &&
                    (pieceAtLocation(row-1,col + 1) != null) &&
                    (pieceAtLocation(row-1,col + 1).getLeftCorner() != t.getMiddleCorner())) {
                bItFits = false ;
                whyItFails = "up & right";
            }

        }

        if (!bItFits)
            Log.Info(this.getClass().getName(), String.format("  Tile '%s' placement @ (%d,%d) fails middle corner test - %s", t, row, col, whyItFails) ) ;
        return bItFits;
    }

    /**
     * Determines if tile 't' can fit in location (row,col) based on the right corner
     *  of the tile fitting with all other pieces on the board adjacent to the new tile
     *  if placed.
     * @param t - new tile to be placed
     * @param row - row for it to be placed in
     * @param col - col for it to be placed in
     * @return true if the tile's right corner fits the location
     */
    protected boolean rightCornerFits(Tile t, int row, int col) {
        boolean bItFits = true;

        String whyItFails = "" ;

        boolean bWeCanLookRight = ( col < num_cols - 1 ) ;
        boolean bWeCanLookFarRight = ( col < num_cols - 2 ) ;
        boolean bWeCanLookDown = ( row < num_rows - 1 ) ;
        boolean bWeCanLookUp = ( row > 0 ) ;

        int cornerToMatch = t.getRightCorner() ;

        // right
        if (bWeCanLookRight) {
            Tile tileToTheRight = pieceAtLocation(row, col + 1) ;
            if ( ( tileToTheRight != null ) &&
                 ( tileToTheRight.getMiddleCorner() != cornerToMatch ) ) {
                Log.Info(this.getClass().getName(),Player.showTwoTilesLeftAndRight(t, pieceAtLocation(row, col + 1)));
                bItFits = false;
                whyItFails = "right";
            }
        }

        // far-right
        if (bWeCanLookFarRight) {
            Tile tileToTheFarRight = pieceAtLocation(row, col + 2) ;
            if ( ( tileToTheFarRight != null ) &&
                ( tileToTheFarRight.getLeftCorner() != cornerToMatch ) ) {
                bItFits = false;
                whyItFails = "far-right";
            }
        }

        // If we are oriented UP, we need to look down...
        if ( t.getOrientation() == Orientation.UP ) {

            // down && far-right
            if ( (bWeCanLookDown) && (bWeCanLookFarRight) ) {
                Tile tileDownAndFarRight = pieceAtLocation(row + 1, col + 2);
                if ( ( tileDownAndFarRight != null) &&
                        ( tileDownAndFarRight.getLeftCorner() != cornerToMatch ) ) {
                    bItFits = false;
                    whyItFails = "down & far-right";
                }
            }

            // down && right
            if ( (bWeCanLookDown) && (bWeCanLookRight) ) {
                Tile tileDownAndRight = pieceAtLocation(row + 1, col + 1) ;
                if ( ( tileDownAndRight != null) &&
                        ( tileDownAndRight.getMiddleCorner() != cornerToMatch ) ) {
                    bItFits = false;
                    whyItFails = "down & right";
                }
            }

        } else {

            // up && right
            if ( (bWeCanLookUp) && (bWeCanLookRight) ) {
                Tile tileUpAndRight = pieceAtLocation(row - 1, col + 1) ;
                    if ( ( tileUpAndRight != null) &&
                            ( tileUpAndRight.getMiddleCorner() != cornerToMatch ) ) {
                        bItFits = false;
                        whyItFails = "up & right";
                    }
            }

            // up && far right
            if ( (bWeCanLookUp) && (bWeCanLookFarRight) ) {
                Tile tileUpAndFarRight = pieceAtLocation(row - 1, col + 2) ;
                if ( ( tileUpAndFarRight != null ) &&
                        ( tileUpAndFarRight.getLeftCorner() != cornerToMatch ) ) {
                    bItFits = false;
                    whyItFails = "up & far-right";
                }
            }
        }
        if (!bItFits)
            Log.Info(this.getClass().getName(), String.format("  Tile '%s' placement @ (%d,%d) rails right corner test - %s", t, row, col, whyItFails) ) ;
        return bItFits;
    }

    /**
     * Determines if the tile being placed at row and col actually fits there
     *   based on the left face of the tile.
     * @param t - tile to be placed
     * @param row - row in which to place it
     * @param col - column in which to place it
     * @return true if tile's left face matches or is adjacent to an empty spot,
     *         false otherwise
     */
    protected Boolean leftFaceFits(Tile t, int row, int col) {

        // This test determines if a tile's (2) '0-0-1' left face fits on the
        //  board when the tile (2) is placed at (row,col) @ (56,57).  The left
        //  face of our tile (2) is 0-1, and tile (7) at position (56,56) has a
        //  right face 1-0.  These faces match because faces are always seen
        //  with the reference point of the center of the tile.
        //
        // ---------------
        //           56   57
        // ---------------
        // |    |= --  7-- ^
        // |    |==\1 P 1//1\
        // | 56 |===\ A // B \
        // |    |====\0//0 T 0\
        // |    |=====v --  2--
        // Assume it fits...
        boolean bItFits = true ;

        // Are we inward of the edge?
        if ( col > 0 ) {

            // Look left...
            Tile tileToOurLeft = pieceAtLocation(row,col-1);

            // Is the left space empty?
            Boolean bOccupied = (tileToOurLeft != null);
            if ( bOccupied ) {

                // Left tile's orientation should be opposite of this one
                // Left tile's right face should match our tile's left face
                Boolean bOrientationSame = (tileToOurLeft.getOrientation() == t.getOrientation());
                Boolean bFacesDoNotMatch = (!tileToOurLeft.getRightFace().match(t.getLeftFace()));

                bItFits = !(bOrientationSame || bFacesDoNotMatch);
            }
        }

        return bItFits;
    }

    /**
     * Determines if the tile being placed at row and col actually fits there
     *   based on the right face of the tile.
     * @param t - tile to be placed
     * @param row - row in which to place it
     * @param col - column in which to place it
     * @return true if tile's right face matches or is adjacent to an empty spot,
     *         false otherwise
     */
    protected Boolean rightFaceFits(Tile t, int row, int col) {

        // Assume it fits...
        boolean bItFits = true ;

        // Are we inward of the edge?
        if ( col < num_cols-1 ) {

            // Look right...
            Tile tileToOurRight = pieceAtLocation(row,col+1);

            // Is the right space empty?
            // Right tile's orientation should be opposite of this one
            // Right tile's left face should match our tile's right face
            if ( ( tileToOurRight != null ) &&
                    ( ( tileToOurRight.getOrientation() == t.getOrientation() ) ||
                            ( !tileToOurRight.getLeftFace().match( t.getRightFace() ) ) ) ) {
                bItFits = false;
            }
        }

        return bItFits;
    }

    /**
     * Determines if the tile being placed at row and col actually fits there
     *   based on the middle face of the tile.
     * @param t - tile to be placed
     * @param row - row in which to place it
     * @param col - column in which to place it
     * @return true if tile's middle face matches or is adjacent to an empty spot,
     *         false otherwise
     */
    protected Boolean middleFaceFits(Tile t, int row, int col) {

        // Assume it fits...
        boolean bItFits = true ;

        // look up?
        if ( t.getOrientation() == Orientation.DOWN ) {

            // If we are inward of the top border...
            if ( row > 0 ) {

                // Look up...
                Tile tileAbove = pieceAtLocation(row-1,col);

                // Is the right space empty?
                // Right tile's orientation should be opposite of this one
                // Right tile's left face should match our tile's right face
                if ( ( tileAbove != null ) &&
                        ( ( tileAbove.getOrientation() == t.getOrientation() ) ||
                                ( !tileAbove.getMiddleFace().match( t.getMiddleFace() ) ) ) ) {
                    bItFits = false;
                }
            }

        } else {

            // If we are inward of the bottom border...
            if ( row < num_rows-1 ) {

                // Look down...
                Tile tileBelow = pieceAtLocation(row+1,col);

                // Is the right space empty?
                // Right tile's orientation should be opposite of this one
                // Right tile's left face should match our tile's right face
                if ( ( tileBelow != null ) &&
                        ( ( tileBelow.getOrientation() == t.getOrientation() ) ||
                                ( !tileBelow.getMiddleFace().match( t.getMiddleFace() ) ) ) ) {
                    bItFits = false;
                }
            }
        }

        return bItFits;
    }

    /**
     * Let's calculate the possible score if this tile is placed where we
     *   want it to be placed.
     * @param t - tile to be placed
     * @param row - position to place the tile
     * @param col - position to place the tile
     * @return  score - score if tile is placed
     */
    protected int calculateScore(Tile t, int row, int col) {

        //***************************************************************
        // Note:  This is a real board, and should be considered
        //        valid.  It is only for demonstration purposes but
        //        resulted from real gameplay.
        //        *******************************************************
        //        For this scoring round, the following will be used:
        //          Piece 42 (2-3-4) completes a hexagon.
        //          Piece 51 (3-4-5) completes a bridge.
        //        *******************************************************
        //  -------------------------------------------------------------
        //            53   54   55   56   57   58   59   60   61   62
        //  -------------------------------------------------------------
        //  |    |+++++=+++++++++=+++++++++^+++++++++=+++++++++=+++++++++
        //  |    |++++===+++++++===+++++++/2\+++++++===+++++++===+++++++
        //  | 52 |+++=====+++++=====+++++/ A \+++++=====+++++=====+++++
        //  |    |++=======+++=======+++/3 P 3\+++=======+++=======+++
        //  |    |+=========+=========+ -- 41-- +=========+=========+
        //  |    |=+++++++++=+++++++++^ -- 48-- ^+++++++++^ -- 56-- =
        //  |    |==+++++++===+++++++/3\\3 P 3//3\+++++++/5\\5 P 5/===
        //  | 53 |===+++++=====+++++/ B \\ A // B \+++++/ A \\ A /=====
        //  |    |====+++=======+++/2 P 4\\4//4 P 4\+++/4 P 5\\5/=======
        //  |    |=====+=========+ -- 42-- v -- 50-- + -- 55-- v=========
        //  |    |+++++^ --  3-- ^ -- 39-- ^ -- 53-- ^ -- 35-- ^ -- 21--
        //  |    |++++/0\\0 P 2//2\\2 P 4//4\\4 P 4//4\\4 P 5//5\\5 P 0/
        //  | 54 |+++/ A \\ A // A \\ B // A \\ B // A \\ B // B \\ B /
        //  |    |++/5 P 0\\0//0 P 2\\2//2 P 4\\4//4 P 1\\1//1 P 5\\5/
        //  |    |+ --  6-- v -- 12-- v -- 44-- v -- 34-- v -- 36-- v
        //  |    |= -- 20-- = -- 15-- ^ -- 45-- ^+++++++++= -- 11-- =
        //  |    |==\5 P 0/===\0 P 2//2\\2 P 4//4\+++++++===\1 P 5/===
        //  | 55 |===\ B /=====\ B // A \\ A // B \+++++=====\ A /=====
        //  |    |====\4/=======\5//5 P 5\\5//5 P 4\+++=======\0/=======
        //  |    |=====v=========v -- 46-- v -- 54-- +=========v=========
        //  |    |+++++= -- 51-- ^ -- 52-- ^+++++++++=+++++++++=+++++++++
        //  |    |++++===\4 P 5//5\\5 P 5//5\+++++++===+++++++===+++++++
        //  | 56 |+++=====\ A // B \\ B // A \+++++=====+++++=====+++++
        //  |    |++=======\3//3 P 3\\3//3 P 0\+++=======+++=======+++
        //  |    |+=========v -- 49-- v -- 18-- +=========+=========+

        // Does this piece complete a bridge?
        //   row = 56
        //   col = 54
        //   Orientation.DOWN
        //     ( ( row > 0 ) && ( col > 0 ) && ( col < num_cols ) &&
        //          ( ( [row-1,col-1] != null ) || ( [row-1,col+1] != null ) || ( [row ) ) ||
        //     ( ( row < num_rows-1 ) && ( col < num_cols-2 ) &&
        //          ( ( [row,  col+2] != null ) || ( [row+1,col+1] != null ) ) ) ||
        //     ( ( row < num_rows-1 ) && ( col < num_cols-2 ) &&
        //          ( ( [row,  col-2] != null ) || ( [row+1,col-1] != null ) ) )
        //
        //   row = 57
        //   col = 58
        //   Orientation.UP
        //     ( ( row < num_rows-1 ) && ( col > 0 ) && ( col < num_cols ) &&
        //          ( ( [row+1,col-1] != null ) || ( [row+1,col+1] != null ) ) ) ||
        //     ( ( row > 0 ) && ( col > 1 ) &&
        //          ( ( [row,  col-2] != null ) || ( [row-1,col-1] != null ) ) ) ||
        //     ( ( row > 0 ) && ( col < num_cols - 2 ) &&
        //          ( ( [row,  col+2] != null
        //
        int score = 0 ;

        boolean bCreatesABridge = false ;

        boolean bULHexagon = false ;
        boolean bURHexagon = false ;
        boolean bUMHexagon = false ;
        boolean bDLHexagon = false ;
        boolean bDRHexagon = false ;
        boolean bDMHexagon = false ;

        boolean bCreatesAHexagon = false ;

        boolean bLeftFaceEmpty ;
        boolean bRightFaceEmpty ;
        boolean bMiddleFaceEmpty ;

        bLeftFaceEmpty  = (col > 0)          && (pieceAtLocation(row, col - 1) == null);
        bRightFaceEmpty = (col < num_cols-1) && (pieceAtLocation(row, col + 1) == null);

        if ( t.getOrientation() == Orientation.UP ) {

            bMiddleFaceEmpty = (row < num_rows-1) && (pieceAtLocation(row + 1, col) == null);

            boolean bAnchorAbove =
                    (
                      ( ( col > 0 )          && ( row > 0 )          && ( pieceAtLocation(row-1, col-1) != null ) ) ||
                      (                         ( row > 0 )          && ( pieceAtLocation(row-1,col) != null ) ) ||
                      ( ( col < num_cols-1 ) && ( row > 0 )          && ( pieceAtLocation(row-1,col+1) != null ) ) ) ;
            boolean bAnchorLeft =
                    (
                      ( ( col > 1 )                                  && ( pieceAtLocation(row, col-2) != null ) ) ||
                      ( ( col > 1 )          && ( row < num_rows-1 ) && ( pieceAtLocation(row+1,col-2 ) != null ) ) ||
                      ( ( col > 0 )          && ( row < num_rows-1 ) && ( pieceAtLocation(row+1,col-1 ) != null ) )
                    ) ;
            boolean bAnchorRight =
                    (
                      ( ( col < num_cols-2 )                         && ( pieceAtLocation(row,col+2) != null ) ) ||
                      ( ( row < num_rows-1 ) && ( col < num_cols-2 ) && ( pieceAtLocation(row+1,col+2) != null ) ) ||
                      ( ( row < num_rows-1 ) && ( col < num_cols-1 ) && ( pieceAtLocation(row+1,col+1) != null ) )
                    ) ;

            if ( ( bLeftFaceEmpty   && bAnchorAbove && bAnchorLeft  ) ||
                 ( bRightFaceEmpty  && bAnchorAbove && bAnchorRight ) ||
                 ( bMiddleFaceEmpty && bAnchorLeft  && bAnchorRight ) ) {
                bCreatesABridge = true ;
            }

            int hexLRD[] = {  0,  0,  1,  1, 1 } ;
            int hexLCD[] = { -1, -2, -2, -1, 0 } ;
            int hexRRD[] = {  1,  1,  1,  0, 0 } ;
            int hexRCD[] = {  0,  1,  2,  2, 1 } ;
            int hexARD[] = {  0, -1, -1, -1, 0 } ;
            int hexACD[] = { -1, -1,  0,  1, 1 } ;

            if ( row < num_rows-1 ) {
                if ( col > 1 ) {
                    bULHexagon = true ;
                    // hexagon left
                    for ( int i=0; i<5; i++ ) {
                        bULHexagon &= ( pieceAtLocation(row + hexLRD[i],col + hexLCD[i]) != null ) ;
                    }
                    if ( bULHexagon )
                        Log.Info(this.getClass().getName(),"  Completed hexagon with Up-Left orientation!");
                }
                if ( col < num_cols-2 ) {
                    bURHexagon = true ;
                    // hexagon right
                    for ( int i=0; i<5; i++ ) {
                        bURHexagon &= ( pieceAtLocation(row + hexRRD[i],col + hexRCD[i]) != null ) ;
                    }
                    if ( bURHexagon )
                        Log.Info(this.getClass().getName(),"  Completed hexagon with Up-Right orientation!");
                }
            }

            if ( row > 1 ) {
                if ( col > 1 && col < num_cols - 1 ) {
                    bUMHexagon = true ;
                    // hexagon above
                    for ( int i=0; i<5; i++ ) {
                        bUMHexagon &= ( pieceAtLocation(row + hexARD[i],col + hexACD[i]) != null ) ;
                    }
                }
                if ( bUMHexagon )
                    Log.Info(this.getClass().getName(),"  Completed hexagon with Up-Middle orientation!");
            }

        } else {

            bMiddleFaceEmpty = (row > 0 ) && (pieceAtLocation(row - 1, col) == null);

            boolean bAnchorBelow =
                    (
                       ( ( col > 0 )          && ( row < num_rows-1 ) && ( pieceAtLocation(row+1, col-1) != null ) ) ||
                       (                         ( row < num_rows-1 ) && ( pieceAtLocation(row+1,col) != null ) ) ||
                       ( ( col < num_cols-1 ) && ( row < num_rows-1 ) && ( pieceAtLocation(row+1,col+1) != null ) )
                    ) ;
            boolean bAnchorLeft =
                    (
                       ( ( col > 1 )                                  && ( pieceAtLocation(row, col-2) != null ) ) ||
                       ( ( col > 1 )          && ( row > 0 )          && ( pieceAtLocation(row-1,col-2 ) != null ) ) ||
                       ( ( col > 0 )          && ( row > 0 )          && ( pieceAtLocation(row-1,col-1 ) != null ) )
                    ) ;
            boolean bAnchorRight =
                    (
                       ( ( col < num_cols-2 )                         && ( pieceAtLocation(row,col+2) != null ) ) ||
                       ( ( col < num_cols-2 ) && ( row > 0 )          && ( pieceAtLocation(row-1,col+2) != null ) ) ||
                       ( ( col < num_cols-1 ) && ( row > 0 )          && ( pieceAtLocation(row-1,col+1) != null ) )
                    ) ;

            if ( ( bLeftFaceEmpty   && bAnchorBelow && bAnchorLeft  ) ||
                 ( bRightFaceEmpty  && bAnchorBelow && bAnchorRight ) ||
                 ( bMiddleFaceEmpty && bAnchorLeft  && bAnchorRight ) ) {
                bCreatesABridge = true ;
            }


            int hexLRD[] = {  0,  0, -1, -1, -1 } ;
            int hexLCD[] = { -1, -2, -2, -1,  0 } ;
            int hexRRD[] = { -1, -1, -1,  0,  0 } ;
            int hexRCD[] = {  0,  1,  2,  2,  1 } ;
            int hexARD[] = {  0, +1, +1, +1,  0 } ;
            int hexACD[] = { -1, -1,  0,  1,  1 } ;

            if ( row > 0 ) {
                if ( col > 1 ) {
                    bDLHexagon = true ;
                    // hexagon left
                    for ( int i=0; i<5; i++ ) {
                        bDLHexagon &= ( pieceAtLocation(row + hexLRD[i],col + hexLCD[i]) != null ) ;
                    }
                    if ( bDLHexagon )
                        Log.Info(this.getClass().getName(),"  Completed hexagon with Down-Left orientation!");
                }
                if ( col < num_cols-2 ) {
                    bDRHexagon = true ;
                    // hexagon right
                    for ( int i=0; i<5; i++ ) {
                        bDRHexagon &= ( pieceAtLocation(row + hexRRD[i],col + hexRCD[i]) != null ) ;
                    }
                    if ( bDRHexagon )
                        Log.Info(this.getClass().getName(),"  Completed hexagon with Down-Right orientation!");
                }
            }

            if ( row < num_rows-1 ) {
                if ( col > 1 && col < num_cols - 1 ) {
                    bDMHexagon = true ;
                    // hexagon below
                    for ( int i=0; i<5; i++ ) {
                        bDMHexagon &= ( pieceAtLocation(row + hexARD[i],col + hexACD[i]) != null ) ;
                    }
                    if ( bDMHexagon )
                        Log.Info(this.getClass().getName(),"  Completed hexagon with Down-Middle orientation!");
                }
            }

        }

        // If any one of six hexagons was created with tile placement, add the bonus!
        bCreatesAHexagon = bULHexagon || bUMHexagon || bURHexagon ||
                           bDLHexagon || bDMHexagon || bDRHexagon ;

        // Hexagon bonus
        if ( bCreatesAHexagon ) {
            Log.Info(this.getClass().getName(),String.format("  Tile %s creates a hexagon @ (%d,%d)!  Bonus of %d points!", t, row, col, HEXAGON_BONUS));
            score = HEXAGON_BONUS ;
        }

        // Bridge bonus
        if ( bCreatesABridge ) {
            Log.Info(this.getClass().getName(),String.format("  Tile %s creates a bridge @ (%d,%d)!  Bonus of %d points!", t, row, col, BRIDGE_BONUS));
            score = BRIDGE_BONUS ;
        }


        // The first tile played
        if (count() == 0) {

            // Starting player can earn 10 points if tile is a triplet
            if (t.isTriplet()) {

                score += 10 ;

                // If three 0's start, there is a 30 point bonus
                if (t.getValue() == 0)
                    score += 30 ;
            }
        }

        // Value of tile face
        score += t.getValue() ;

        return score;
    }


    /**
     * Places the provided tile on the board at the location specified, if and
     *   only if, it will fit.
     * @param choice - the choice we've made to place a tile
     * @return true if placed, false otherwise.
     */
    public boolean placeTile( Choice choice ) {

        int row = choice.getRow();
        int col = choice.getCol();

        if ( row < 0 || col < 0 || row > num_rows-1 || col > num_cols-1)
            return false ;

        Tile t = choice.getTile();

        // Orientation needs to be set based on the tile position.
        // Do it now so the checks and balances can work.
        t.setOrientation( getOrientationForPositionOnBoard(row,col) ) ;

        // Does the piece fit into that location on the board?
        if ( pieceFits( t, row, col, choice ) ) {

            // Play the tile and setup the piece information.
            playedTiles[row][col] = t;

            // Set the row and column and player
            t.setRow(row);
            t.setCol(col);

            // Placed on the board
            t.setPlaced(true) ;

            // Removed from the player's tray
            t.setInTray(false) ;
            return true;
        }
        return false;
    }

    /**
     * Draws the horizontal scale for the board (across the top)
     * @return (String) a String representation of the column scale
     */
    private String drawColumnScale() {

        StringBuilder strScale = new StringBuilder(120);
        strScale.append(p+"------");
        // Let's paint the rows and column numbers for reference
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append(p+"---------");
        strScale.append("\n        ");
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append(p+String.format(" %3d ", col));
        strScale.append(p+"\n------");
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append(p+"---------");
        strScale.append("\n"+r);

        return strScale.toString() ;
    }

    /**
     * Determines if the value provided is an even number or not
     * @param val (int) the value to determine oddness
     * @return true if even; false otherwise
     */
    private Boolean even(int val) {
        return ((val%2)==0);
    }

    /**
     * Draws a single slice (row) of the vertical scale for the board (left side)
     *   Both the row and column are needed to determine which direction the
     *   spacers go for each sub-row.
     * @param strScale (String[]) array of Strings representing a single row
     * @param row - The value of the row this scale is for.
     * @param col - The value of the column this scale is for.
     */
    private void drawRowScale(String[] strScale, int row, int col) {
        if (even(row + col)) {
            strScale[0] += p+"|    |"+c+"="    ;
            strScale[1] += p+"|    |"+c+"=="   ;
            strScale[2] += String.format(
                           p+"|%3d |"+c+"==="  ,row);
            strScale[3] += p+"|    |"+c+"====" ;
            strScale[4] += p+"|    |"+c+"=====";
        } else {
            strScale[0] += p+"|    |"+c+"+++++";
            strScale[1] += p+"|    |"+c+"++++" ;
            strScale[2] += String.format(
                           p+"|%3d |"+c+"+++"  ,row);
            strScale[3] += p+"|    |"+c+"++"   ;
            strScale[4] += p+"|    |"+c+"+"    ;
        }
    }

    /**
     * Constructs the String representation of just the board
     * @param full - true to show the whole board, false to show only the smallest
     *             size necessary.
     * @return (String) a String representation of just the board and placed tiles.
     */
    protected String display(boolean full) {

        StringBuilder strBoard = new StringBuilder(700);

        findBoardMinMax( full );

        // Display a row at a time, so
        for (int row = topBorder; row <= bottomBorder; row++) {

            // Let's paint the rows and column numbers for reference
            if ( row == topBorder ) {
                strBoard.append(drawColumnScale());
            }

            String strRow[] = new String[5];
            for (int i = 0; i < 5; i++) {
                strRow[i]="";
            }

            for (int col = leftBorder; col <= rightBorder; col++) {

                // If we are starting a new row
                if (col == leftBorder) {
                    drawRowScale(strRow,row,col);
                }

                // If there is no tile here, let's fill it with empty space
                if (playedTiles[row][col] == null) {
                    if ( even(row+col) ) {
                        strRow[0] += c+"+++++++++" ;
                        strRow[1] += c+"+++++++"   ;
                        strRow[2] += c+"+++++"     ;
                        strRow[3] += c+"+++"       ;
                        strRow[4] += c+"+"         ;
                    } else {
                        strRow[0] += c+"="         ;
                        strRow[1] += c+"==="       ;
                        strRow[2] += c+"====="     ;
                        strRow[3] += c+"======="   ;
                        strRow[4] += c+"=========" ;
                    }
                } else {
                    playedTiles[row][col].draw(false, strRow);
                }
            }

            if (even(row+rightBorder)) {
                strRow[0] += c+"="     ;
                strRow[1] += c+"=="    ;
                strRow[2] += c+"==="   ;
                strRow[3] += c+"===="  ;
                strRow[4] += c+"=====" ;
            } else {
                strRow[0] += c+"+++++" ;
                strRow[1] += c+"++++"  ;
                strRow[2] += c+"+++"   ;
                strRow[3] += c+"++"    ;
                strRow[4] += c+"+"     ;
            }
            for ( int k=0; k<5; k++) {
                strBoard.append(strRow[k]);
                strBoard.append("\n"+r);
            }
        }
        return strBoard.toString();
    }

    /**
     * Displays a version of the complete board and its values.
     * @return (String) a String representation of the current state of gameplay on
     *                  the board.
     */
    public String toString() {
        findBoardMinMax(false);
        String board = String.format(
                "Board:\n  Played Piece Count:%d\n  Boundaries:(%d,%d,%d,%d)\n",
                count(), topBorder, bottomBorder, leftBorder, rightBorder);
        return (board + display(false));
    }
}
