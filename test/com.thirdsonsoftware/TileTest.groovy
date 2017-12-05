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

/** Represents a tile in the game.
 * @author Andrew B. Montcrieff
 * @author www.ThirdSonSoftware.com
 * @version 0.1
 * @since 0.0
 */
class TileTest extends GroovyTestCase {

    static int TEST_ROTATE = 240

    static int CORNER_C = 4
    static int CORNER_B = 3
    static int CORNER_A = 2

    static int EXPECTED_ROTATION = 0

    static int TILE_ROW = 56
    static int TILE_COL = 56

    static int TILE_SET_ROW = 25
    static int TILE_SET_COL = 31

    Tile tile = null

    Tile tripletTile = null

    static int TRIPLET_CORNER = 3
    static int TRIPLET_ROTATION = 120

    static int TRIPLET_TILE_ROW = 5
    static int TRIPLET_TILE_COL = 10

    void setUp() {
        super.setUp()

        tile = new Tile(CORNER_A,CORNER_B,CORNER_C)
        tile.placed = true
        tile.rotation = EXPECTED_ROTATION
        tile.orientation = Orientation.UP
        tile.row = TILE_ROW
        tile.col = TILE_COL
        tile.inTray = false

        tripletTile = new Tile (TRIPLET_CORNER,TRIPLET_CORNER,TRIPLET_CORNER)
        tripletTile.placed = false
        tripletTile.rotation = TRIPLET_ROTATION
        tripletTile.orientation = Orientation.DOWN
        tripletTile.row = TRIPLET_TILE_ROW
        tripletTile.col = TRIPLET_TILE_COL
        tripletTile.inTray = true
    }

    void tearDown() {
    }

    void testSetRow() {
        tile.setRow(TILE_SET_ROW)
        assertEquals(TILE_SET_ROW, tile.getRow());
    }

    void testGetRow() {
        assertEquals(TILE_ROW, tile.getRow())
        assertEquals(TRIPLET_TILE_ROW, tripletTile.getRow())
    }

    void testSetCol() {
        tile.setCol(TILE_SET_COL)
        assertEquals(TILE_SET_COL,tile.getCol())
    }

    void testGetCol() {
        assertEquals(TILE_COL, tile.getCol())
        assertEquals(TRIPLET_TILE_COL, tripletTile.getCol())
    }

    void testGetCornerA() {
        assertEquals(CORNER_A, tile.getCornerA())
        assertEquals(TRIPLET_CORNER, tripletTile.getCornerA())
    }

    void testGetCornerB() {
        assertEquals(CORNER_B, tile.getCornerB())
        assertEquals(TRIPLET_CORNER, tripletTile.getCornerB())
    }

    void testGetCornerC() {
        assertEquals(CORNER_C, tile.getCornerC())
        assertEquals(TRIPLET_CORNER, tripletTile.getCornerC())

    }

    void testSetCornerC() {
        tile.setCornerC(1);
        assertEquals(1,tile.getCornerC())
    }

    void testGetRotation() {
        assertEquals(EXPECTED_ROTATION,tile.getRotation())
        assertEquals(TRIPLET_ROTATION, tripletTile.getRotation())
    }

    void testSetRotation() {
        tripletTile.rotate(TEST_ROTATE)
        assertEquals(TEST_ROTATE,tripletTile.getRotation())
    }

    void testGetOrientation() {
        assertEquals( Orientation.UP, tile.getOrientation())
        assertEquals( Orientation.DOWN, tripletTile.getOrientation())
    }

    void testSetOrientation() {
        assertEquals( Orientation.DOWN, tripletTile.getOrientation())
        tripletTile.setOrientation(Orientation.UP)
        assertEquals( Orientation.UP, tripletTile.getOrientation())
    }

    void testGetValue() {
        assertEquals(9,tile.getValue())
        assertEquals(9,tripletTile.getValue())
    }

    void testSetValue() {
        assertEquals(9,tripletTile.getValue())
        tripletTile.setValue(7)
        tripletTile.cornerC = 2
        assertEquals(7, tripletTile.getValue())
    }

    void testToString() {
        String a = String.format("%d-%d-%d",CORNER_A,CORNER_B,CORNER_C)
        String b = tile
        assertEquals(a, b)

        a = String.format("%d-%d-%d",TRIPLET_CORNER,TRIPLET_CORNER,TRIPLET_CORNER)
        b = tripletTile
        assertEquals(a, b)
    }

    void testIsTriplet() {
        assertFalse(tile.isTriplet())
        assertTrue(tripletTile.isTriplet())
    }

    void testCompareTo() {
        assertEquals( -1, tile <=> tripletTile)
        assertEquals(0, tripletTile <=> tripletTile)
    }

    void testRotate() {
        tripletTile.rotate(TEST_ROTATE)
        assertEquals(TEST_ROTATE,tripletTile.getRotation())
    }

    void testOrient() {
        tripletTile.orient(Orientation.UP)
        assertEquals(Orientation.UP, tripletTile.orientation)
    }

    void testDrawTileDown() {
        String[] row = new String[5]
        for (int i=0;i<5;i++)
            row[i]=""
        Board board = new Board(1,1)
        board.placeTile(tile,0,0)

        tile.draw(true,row)

        assertEquals("-------",  row[0])
        assertEquals("\\3   4/", row[1])
        assertEquals(" \\   /",  row[2])
        assertEquals("  \\2/",   row[3])
        assertEquals("   v ",    row[4])
    }

    void testDrawTileUp() {
        String[] row = new String[5]
        for (int i=0;i<5;i++)
            row[i]=""
        Board board = new Board(1,2)
        board.placeTile(tile,0,1)

        tile.draw(true,row)

        assertEquals("   ^ ",    row[0])
        assertEquals("  /2\\",   row[1])
        assertEquals(" /   \\",  row[2])
        assertEquals("/4   3\\", row[3])
        assertEquals("-------",  row[4])
    }

    void testGetLeftCorner() {
        assertEquals(CORNER_C,tile.getLeftCorner())
        tile.rotate(120)
        assertEquals(CORNER_B,tile.getLeftCorner())
        tile.rotate(240)
        assertEquals(CORNER_A,tile.getLeftCorner())
    }

    void testGetRightCorner() {
        assertEquals(CORNER_B,tile.getRightCorner())
        tile.rotate(120)
        assertEquals(CORNER_A,tile.getRightCorner())
        tile.rotate(240)
        assertEquals(CORNER_C,tile.getRightCorner())
    }

    void testGetMiddleCorner() {
        assertEquals(CORNER_A,tile.getMiddleCorner())
        tile.rotate(120)
        assertEquals(CORNER_C,tile.getMiddleCorner())
        tile.rotate(240)
        assertEquals(CORNER_B,tile.getMiddleCorner())
    }

    void testGetPlaced() {
        assertEquals(true,tile.getPlaced())
        assertEquals(false,tripletTile.getPlaced())
    }

    void testSetTray() {
        tile.setInTray(false)
        assertFalse(tile.getInTray())
        tripletTile.setInTray(true)
        assertTrue(tripletTile.getInTray())
    }

    void testGetTray() {
        assertFalse(tile.getInTray())
        assertTrue(tripletTile.getInTray())
    }

    void testGetLeftFace() {
        assertEquals(new Face(CORNER_C,CORNER_A),tile.getLeftFace())
    }

    void testGetRightFace() {
        assertEquals(new Face(CORNER_A,CORNER_B),tile.getRightFace())
    }

    void testGetMiddleFace() {
        assertEquals(new Face(TRIPLET_CORNER,TRIPLET_CORNER),tripletTile.getMiddleFace())
    }

    void testGetPlayer() {
        assertEquals(null,tile.getPlayer())
        tile.setPlayer(new Player("TestGet"))
        assertEquals("TestGet",tile.getPlayer().getName())
    }

    void testSetPlayer() {
        assertEquals(null,tile.getPlayer())
        tile.setPlayer(new Player("TestSet"))
        assertEquals("TestSet",tile.getPlayer().getName())
    }
}
