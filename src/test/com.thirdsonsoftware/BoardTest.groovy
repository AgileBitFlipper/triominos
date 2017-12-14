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

package com.thirdsonsoftware

class BoardTest extends GroovyTestCase {

    protected final int NUM_ROWS = 2
    protected final int NUM_COLS = 2

    protected final int TILE_A_ROW = 56
    protected final int TILE_A_COL = 56
    protected final int TILE_B_ROW = 56
    protected final int TILE_B_COL = 55

    Board board = new Board()

    Tile tileA = new Tile(0,0,0)
    Tile tileB = new Tile(0,0,1)
    Tile tileC = new Tile(1,1,5)
    Tile tileD = new Tile(0,1,1)
    Tile tileE = new Tile(0,0,5)

    Choice choiceA = new Choice(tileA,TILE_A_ROW,TILE_A_COL,Orientation.DOWN,0)
    Choice choiceB = new Choice(tileB,TILE_B_ROW,TILE_B_COL,Orientation.UP,0)
    Choice choiceC = new Choice(tileC,57,54,Orientation.UP,0)
    Choice choiceD = new Choice(tileD,57,55,Orientation.DOWN,240)
    Choice choiceE = new Choice(tileE,56,57,Orientation.UP,240)

    void setUp() {
        super.setUp()
        assertTrue(board.placeTile( choiceA ))

        tileB.setRotation(240)
        assertTrue(board.placeTile( choiceB ))
    }

    void tearDown() {
    }

    void testPieceAtLocation() {
        Tile tileD = board.pieceAtLocation(TILE_A_ROW, TILE_A_COL)
        assertEquals("Placed nonTripletTile is not the one expected.", tileA, tileD)
    }

    void testPlaceTile() {
        board.placeTile(choiceC)
        Tile tileTemp = getBoard().pieceAtLocation(choiceC.getRow(),choiceC.getCol())
        assertEquals(tileC, tileTemp)
    }

    void testDisplay() {
        board.display(false)

    }

    void testClearBoard() {
        assertTrue(board.count()>0)
        board.clearBoard()
        assertTrue(board.count()==0)
    }

    void testCount() {
        assertEquals(2,board.count())
    }

    void testToString() {
        System.out.println(board.toString())
        String strExpected = \
        "Board:\n"+
        "  Played Piece Count:2\n"+
        "  Boundaries:(56,56,55,56)\n"+
        "------------------------\n"+
        "          55   56 \n"+
        "------------------------\n"+
        "|    |+++++^ --  0-- =\n"+
        "|    |++++/0\\\\0 P 0/==\n"+
        "| 56 |+++/   \\\\   /===\n"+
        "|    |++/1 P 0\\\\0/====\n"+
        "|    |+ --  0-- v=====\n"

        String strActual = board.toString()
        assertEquals(strExpected,strActual)
    }

    void testGetNumberOfRows() {
        assertEquals(Board.DEFAULT_ROWS,board.getNumberOfRows())
    }

    void testSetNumberOfRows() {
        assertEquals(Board.DEFAULT_ROWS, board.getNumberOfRows())
        board.setNumberOfRows(NUM_ROWS)
        assertEquals(NUM_ROWS,board.getNumberOfRows())
    }

    void testGetNumberOfCols() {
        assertEquals(Board.DEFAULT_COLS,board.getNumberOfCols())
    }

    void testSetNumberOfCols() {
        assertEquals(Board.DEFAULT_COLS,board.getNumberOfCols())
        board.setNumberOfCols(NUM_COLS)
        assertEquals(NUM_COLS,board.getNumberOfCols())
    }

    void testGetTopBorderFull() {
        board.findBoardMinMax(true)
        int top = board.getTopBorder()
        assertEquals(0, board.getTopBorder())
        int left = board.getLeftBorder()
        assertEquals(0, board.getLeftBorder())
        int right = board.getRightBorder()
        assertEquals(Board.DEFAULT_COLS, board.getRightBorder())
        int bottom = board.getBottomBorder()
        assertEquals(Board.DEFAULT_ROWS, board.getBottomBorder())

        System.out.println(String.format("Top:%d\nLeft:%d\nRight:%d\nBottom:%d",top,left,right,bottom))
    }

    void testGetTopBorderNotFull() {
        board.findBoardMinMax(false)
        int top = board.getTopBorder()
        assertEquals(TILE_A_ROW,board.getTopBorder())
        int left = board.getLeftBorder()
        assertEquals(TILE_B_COL,board.getLeftBorder())
        int right = board.getRightBorder()
        assertEquals(TILE_A_COL,board.getRightBorder())
        int bottom = board.getBottomBorder()
        assertEquals(TILE_B_ROW,board.getBottomBorder())

        System.out.println(String.format("Top:%d\nLeft:%d\nRight:%d\nBottom:%d",top,left,right,bottom))
    }

    void testSetTopBorder() {
        board.findBoardMinMax(false)
        final int NEW_ROW = 5
        assertEquals(TILE_A_ROW,board.getTopBorder())
        board.setTopBorder(NEW_ROW)
        assertEquals(NEW_ROW,board.getTopBorder())
    }

    void testSetTopBorderTooLow() {
        final int TOO_LOW = -5
        board.setTopBorder(TOO_LOW)
        assertEquals(0,board.getTopBorder())
    }

    void testSetTopBorderTooHigh() {
        final int TOO_HIGH = 125
        board.setTopBorder(TOO_HIGH)
        assertEquals(Board.DEFAULT_ROWS-1,board.getTopBorder())
    }

    void testGetBottomBorder() {
        assertEquals(0,board.getBottomBorder())
        board.findBoardMinMax(false)
        assertEquals(TILE_A_ROW,board.getBottomBorder())
    }

    void testSetBottomBorder() {
        final int NEW_ROW = 66
        board.findBoardMinMax(false)
        assertEquals(TILE_A_ROW,board.getBottomBorder())
        board.setBottomBorder(NEW_ROW)
        assertEquals(NEW_ROW,board.getBottomBorder())
    }

    void testSetBottomBorderTooLow() {
        int TOO_LOW = -15
        board.findBoardMinMax(false)
        board.setBottomBorder(TOO_LOW)
        assertEquals(0,board.getBottomBorder())
    }

    void testSetBottomBorderTooHigh() {
        int TOO_HIGH = 200
        board.findBoardMinMax(false)
        board.setBottomBorder(TOO_HIGH)
        assertEquals(Board.DEFAULT_ROWS-1,board.getBottomBorder())
    }

    void testGetLeftBorder() {
        board.findBoardMinMax(false)
        assertEquals(TILE_B_COL,board.getLeftBorder())
    }

    void testSetLeftBorder() {
        int NEW_LEFT_BORDER = 5
        board.setLeftBorder(NEW_LEFT_BORDER)
        assertEquals(NEW_LEFT_BORDER,board.getLeftBorder())
    }

    void testSetLeftBorderTooLow() {
        int LEFT_BORDER_TOO_LOW = -2
        board.setLeftBorder(LEFT_BORDER_TOO_LOW)
        assertEquals(112,board.getLeftBorder())
    }

    void testSetLeftBorderTooHigh() {
        int LEFT_BORDER_TOO_HIGH = 225
        board.setLeftBorder(LEFT_BORDER_TOO_HIGH)
        assertEquals(Board.DEFAULT_COLS-1,board.getLeftBorder())
    }
    void testGetRightBorder() {
        board.findBoardMinMax()
        assertEquals(TILE_A_COL, board.getRightBorder())
    }

    void testSetRightBorder() {
        int NEW_RIGHT_BORDER = 125
        board.setRightBorder(NEW_RIGHT_BORDER)
        assertEquals(Board.DEFAULT_COLS-1,board.getRightBorder())
    }

    void testSetRightBorderTooLow() {
        int RIGHT_BORDER_TOO_LOW = -55
        board.setRightBorder(RIGHT_BORDER_TOO_LOW)
        assertEquals(0,board.getRightBorder())
    }

    void testSetRightBorderTooHigh() {
        int RIGHT_BORDER_TOO_HIGH = 254
        board.setRightBorder(RIGHT_BORDER_TOO_HIGH)
        assertEquals(Board.DEFAULT_COLS-1,board.getRightBorder())
    }

    void testLeftFaceFits() {
        assertTrue(board.leftFaceFits(tileD,56,58))
        tileD.rotate(120)
        assertTrue(board.leftFaceFits(tileD,56,58))
        tileD.rotate(240)
        assertTrue(board.leftFaceFits(tileD,56,58))
    }

    void testRightFaceFits() {
        tileE.setOrientation(Orientation.UP)
        assertTrue(board.rightFaceFits(tileE,56,55))
        tileE.rotate(120)
        assertFalse(board.rightFaceFits(tileE,56,55))
        tileE.rotate(240)
        assertFalse(board.rightFaceFits(tileE,56,55))
    }

    void testMiddleFaceFits() {
        tileE.setOrientation(Orientation.UP)
        assertFalse(board.middleFaceFits(tileE,55,56))
        tileE.rotate(120)
        assertTrue(board.middleFaceFits(tileE,55,56))
        tileE.rotate(240)
        assertFalse(board.middleFaceFits(tileE,55,56))
    }

    void testPieceFits() {
        choiceE.setOrientation(Orientation.UP)
        assertTrue(board.pieceFits(tileE,56,57, choiceE))
        choiceE.setRotation(120)
        assertFalse(board.pieceFits(tileE, 56, 57, choiceE))
        choiceE.setRotation(240)
        assertTrue(board.pieceFits(tileE, 56,57, choiceE))
    }

    void testFindBoardMinMax() {
        board.findBoardMinMax()
        assertEquals(55,board.getLeftBorder())
        assertEquals(56,board.getRightBorder())
        assertEquals(56,board.getTopBorder())
        assertEquals(56,board.getBottomBorder())

        tileE.rotate(120)
        board.placeTile(choiceE)
        board.findBoardMinMax()
        assertEquals(55,board.getLeftBorder())
        assertEquals(57,board.getRightBorder())
        assertEquals(56,board.getTopBorder())
        assertEquals(56,board.getBottomBorder())

        System.out.println(board.display(false))
    }
}
