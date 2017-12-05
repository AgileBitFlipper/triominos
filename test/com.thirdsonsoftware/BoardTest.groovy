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

import com.sun.org.apache.bcel.internal.generic.NEW

class BoardTest extends GroovyTestCase {

    protected final int NUM_ROWS = 2
    protected final int NUM_COLS = 2

    protected final int TILEA_ROW = 56
    protected final int TILEA_COL = 56
    protected final int TILEB_ROW = 56
    protected final int TILEB_COL = 57

    Board board = new Board()

    Tile tileA = new Tile(0,0,0)
    Tile tileB = new Tile(0,0,1)
    Tile tileC = new Tile(1,1,5)
    Tile tileD = new Tile(0,1,1)
    Tile tileE = new Tile(0,0,5)


    void setUp() {
        super.setUp()
        assertTrue(board.placeTile( tileA, TILEA_ROW, TILEA_COL ))

        tileB.setRotation(240)
        assertTrue(board.placeTile( tileB, TILEB_ROW, TILEB_COL ))
    }

    void tearDown() {
    }

    void testPieceAtLocation() {
        Tile tileD = board.pieceAtLocation(TILEA_ROW, TILEA_COL)
        assertEquals("Placed nonTripletTile is not the one expected.", tileA, tileD)
    }

    void testPlaceTile() {
        board.placeTile( tileC,0,0)
        Tile tileD = getBoard().pieceAtLocation(0,0)
        assertEquals("Placed nonTripletTile is not the one expected.", tileC, tileD)
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
        System.out.println(board)
        String strExpected = \
            "Board:\n" +
            "  Played Piece Count:2\n" +
            "  Boundaries:(0,112,0,112)\n" +
            "------- ^ \n" +
            "\\0   0//0\\\n" +
            " \\   //   \\\n" +
            "  \\0//0   1\\\n" +
            "   v -------\n\n"

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
        assertEquals(TILEA_ROW,board.getTopBorder())
        int left = board.getLeftBorder()
        assertEquals(TILEA_COL,board.getLeftBorder())
        int right = board.getRightBorder()
        assertEquals(TILEB_COL,board.getRightBorder())
        int bottom = board.getBottomBorder()
        assertEquals(TILEB_ROW,board.getBottomBorder())

        System.out.println(String.format("Top:%d\nLeft:%d\nRight:%d\nBottom:%d",top,left,right,bottom))
    }

    void testSetTopBorder() {
        board.findBoardMinMax(false)
        final int NEW_ROW = 5
        assertEquals(TILEA_ROW,board.getTopBorder())
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
        assertEquals(TILEA_ROW,board.getBottomBorder())
    }

    void testSetBottomBorder() {
        final int NEW_ROW = 66
        board.findBoardMinMax(false)
        assertEquals(TILEA_ROW,board.getBottomBorder())
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
        assertEquals(TILEA_COL,board.getLeftBorder())
    }

    void testSetLeftBorder() {
        int NEW_LEFT_BORDER = 5
        board.setLeftBorder(NEW_LEFT_BORDER)
        assertEquals(NEW_LEFT_BORDER,board.getLeftBorder())
    }

    void testSetLeftBorderTooLow() {
        int LEFT_BORDER_TOO_LOW = -2
        board.setLeftBorder(LEFT_BORDER_TOO_LOW)
        assertEquals(0,board.getLeftBorder())
    }

    void testSetLeftBorderTooHigh() {
        int LEFT_BORDER_TOO_HIGH = 225
        board.setLeftBorder(LEFT_BORDER_TOO_HIGH)
        assertEquals(Board.DEFAULT_COLS-1,board.getLeftBorder())
    }
    void testGetRightBorder() {
        board.findBoardMinMax()
        assertEquals(TILEB_COL, board.getRightBorder())
    }

    void testSetRightBorder() {
        int NEW_RIGHT_BORDER = 125
        board.setRightBorder(NEW_RIGHT_BORDER)
        assertEquals(NEW_RIGHT_BORDER,board.getRightBorder())
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
        assertFalse(board.leftFaceFits(tileD,56,58))
        tileD.rotate(120)
        assertTrue(board.leftFaceFits(tileD,56,58))
        tileD.rotate(240)
        assertFalse(board.leftFaceFits(tileD,56,58))
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
        tileE.setOrientation(Orientation.UP)
        assertFalse(board.pieceFits(tileE,55,56))
        tileE.rotate(120)
        assertTrue(board.pieceFits(tileE, 55,56))
        tileE.rotate(240)
        assertFalse(board.pieceFits(tileE, 55, 56))
    }

    void testFindBoardMinMax() {
        board.findBoardMinMax()
        assertEquals(56,board.getLeftBorder())
        assertEquals(57,board.getRightBorder())
        assertEquals(56,board.getTopBorder())
        assertEquals(56,board.getBottomBorder())

        tileE.rotate(120)
        board.placeTile(tileE,55,56)
        board.findBoardMinMax()
        assertEquals(56,board.getLeftBorder())
        assertEquals(57,board.getRightBorder())
        assertEquals(55,board.getTopBorder())
        assertEquals(56,board.getBottomBorder())

        System.out.println(board.display(false))
    }
}
