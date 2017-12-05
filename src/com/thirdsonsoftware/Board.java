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

public class Board {

    protected static final int DEFAULT_ROWS = 112 ;
    protected static final int DEFAULT_COLS = 112 ;

    private int num_rows ;
    private int num_cols ;

    private int topBorder    = DEFAULT_ROWS ;
    private int bottomBorder ;
    private int leftBorder   = DEFAULT_COLS;
    private int rightBorder  ;

    final Tile[][] playedTiles ;

    public Board() {
        playedTiles = new Tile[DEFAULT_ROWS][DEFAULT_COLS];
        num_cols=DEFAULT_COLS;
        num_rows=DEFAULT_ROWS;
        clearBoard();
    }

    public Board( int rows, int cols ) {
        playedTiles = new Tile[rows][cols] ;
        num_rows=rows;
        num_cols=cols;
        clearBoard();
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
     * Determines if the tile being placed at row and col actually fits there
     *   based on the left face of the tile.
     * @param t - tile to be placed
     * @param row - row in which to place it
     * @param col - column in which to place it
     * @return true if tile's left face matches or is adjacent to an empty spot,
     *         false otherwise
     */
    protected Boolean leftFaceFits(Tile t, int row, int col) {

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
                Boolean bFacesDontMatch = (!tileToOurLeft.getRightFace().match(t.getLeftFace()));

                bItFits = !(bOrientationSame || bFacesDontMatch);
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
     * Verifies that the current space is empty, and that all of the
     *   adjacent faces and corners will allow it to be played there.
     * @param t - the tile to determine if it fits
     * @param row - the row of the location to place the tile
     * @param col - the column of the location to place the tile
     * @return true if it fits, and false otherwise
     */
    public boolean pieceFits(Tile t, int row, int col) {

        boolean bItFits = false ;

        // Is the slot empty?
        if ( playedTiles[row][col] == null )
        {
            bItFits = leftFaceFits(t,row,col)  &&
                      rightFaceFits(t,row,col) &&
                      middleFaceFits(t,row,col) ;
        }
        return bItFits ;
    }

    /**
     * Places the provided tile on the board at the location specified, if and
     *   only if, it will fit.
     * @param t - tile to place on board
     * @param row - row of where to place it
     * @param col - column of where to place it
     * @return true if placed, false otherwise.
     */
    public boolean placeTile(Tile t, int row, int col ) {

        // Orientation needs to be set based on the tile position.
        // Do it now so the checks and balances can work.
        t.setOrientation( ( ( ( row + col ) % 2 ) == 0 ) ? Orientation.DOWN : Orientation.UP ) ;

        // Does the piece fit into that location on the board?
        if ( pieceFits( t, row, col ) ) {

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
     * Overload for findBoardMinMax(boolean)
     */
    protected void findBoardMinMax() {
        findBoardMinMax(false);
    }

    /**
     * @param full - If true, use the set rows and cols.  If false, look for the
     *             size of the board and only show the smallest version of the board that
     *             shows all tiles.
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

    public String toString() {
        findBoardMinMax(false);
        String board = String.format(
                "Board:\n  Played Piece Count:%d\n  Boundaries:(%d,%d,%d,%d)\n",
                count(), topBorder, bottomBorder, leftBorder, rightBorder);
        return (board + display(false));
    }

    private Boolean even(int val) {
        return ((val%2)==0);
    }

    String drawColumnScale() {
        StringBuilder strScale = new StringBuilder(120);
        strScale.append("------");
        // Let's paint the rows and column numbers for reference
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append("--------");
        strScale.append("\n      ");
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append(String.format(" %3d ", col));
        strScale.append("\n------");
        for (int col = leftBorder; col <= rightBorder; col++)
            strScale.append("--------");
        strScale.append("\n");

        return strScale.toString() ;
    }

    void drawRowScale(String[] strScale, int row, int col) {
        if (even(row + col)) {
            strScale[0] += "|    |=";
            strScale[1] += "|    |==";
            strScale[2] += String.format("|%3d |===",row);
            strScale[3] += "|    |====";
            strScale[4] += "|    |=====";
        } else {
            strScale[0] += "|    |+++++";
            strScale[1] += "|    |++++";
            strScale[2] += String.format("|%3d |+++",row);
            strScale[3] += "|    |++";
            strScale[4] += "|    |+";
        }
    }

    String display(boolean full) {

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
                        strRow[0] += "+++++++++";
                        strRow[1] += "+++++++";
                        strRow[2] += "+++++";
                        strRow[3] += "+++";
                        strRow[4] += "+";
                    } else {
                        strRow[0] += "=";
                        strRow[1] += "===";
                        strRow[2] += "=====";
                        strRow[3] += "=======";
                        strRow[4] += "=========";
                    }
                } else {
                    playedTiles[row][col].draw(false, strRow);
                }
            }

            for ( int k=0; k<5; k++) {
                strBoard.append(strRow[k]);
                strBoard.append("\n");
            }
        }
        return strBoard.toString();
    }

    public int getNumberOfRows() {
        return num_rows;
    }

    public void setNumberOfRows(int num_rows) {
        this.num_rows = num_rows;
    }

    public int getNumberOfCols() {
        return num_cols;
    }

    public void setNumberOfCols(int num_cols) {
        this.num_cols = num_cols;
    }

    public int getTopBorder() {
        return topBorder;
    }

    public void setTopBorder(int topBorder) {
        if ( topBorder < 0 ) {
            setTopBorder(0);
        } else if ( topBorder >= getNumberOfRows() ) {
            setTopBorder(getNumberOfRows() - 1);
        } else {
            this.topBorder = topBorder;
        }
    }

    public int getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(int bottomBorder) {
        if ( bottomBorder < 0 ) {
            setBottomBorder(0);
        } else if ( bottomBorder >= getNumberOfRows() ) {
            setBottomBorder(getNumberOfRows() - 1);
        } else {
            this.bottomBorder = bottomBorder;
        }
    }

    public int getLeftBorder() {
        return leftBorder;
    }

    public void setLeftBorder(int leftBorder) {
        if ( leftBorder < 0 ) {
            setBottomBorder(0);
        } else if ( leftBorder >= getNumberOfCols() ) {
            setLeftBorder(getNumberOfCols()-1);
        } else {
            this.leftBorder = leftBorder;
        }
    }

    public int getRightBorder() {
        return rightBorder;
    }

    public void setRightBorder(int rightBorder) {
        if ( rightBorder < 0 ) {
            setRightBorder(0);
        } else if ( rightBorder >= getNumberOfCols() ) {
            setRightBorder(getNumberOfCols()-1);
        } else {
            this.rightBorder = rightBorder;
        }
    }

}
