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

class PlayerTest extends GroovyTestCase {

    static int TEST_SCORE = 25
    static int UNEXPECTED_SCORE = 111
    static String UNEXPECTED_NAME = "CoolCatLewis"
    static String TEST_NAME = "PlayerX"
    static Tile fiveFivesTile = new Tile(5,5,5)
    static Board board = new Board()
    ArrayList<Tile> availableFaces = new ArrayList<Tile>()
    ArrayList<Tile> playedTiles = new ArrayList<Tile>()

    Player player = null

    void setUp() {
        super.setUp()
        player = new Player( UNEXPECTED_NAME )
        player.score = UNEXPECTED_SCORE
        player.tray.add( new Tile(1,1,1) )
        player.tray.add( new Tile(2,2,2) )
        player.tray.add( new Tile(3,3,3) )
        player.tray.add( new Tile(4,4,4) )
        player.tray.add( fiveFivesTile )

        availableFaces.add(new Tile(1,2,3))
        availableFaces.add(new Tile(1,2,1))

        playedTiles.add(new Tile(1,2,3))
        playedTiles.add(new Tile(1,2,1))
    }

    void tearDown() {
    }

    void testGetScore() {
        assertEquals( UNEXPECTED_SCORE, player.getScore() )
    }

    void testSetScore() {
        player.setScore(TEST_SCORE)
        assertEquals(TEST_SCORE, player.getScore())
    }

    void testGetName() {
        assertEquals(UNEXPECTED_NAME,player.getName())
    }

    void testSetName() {
        player.setName(TEST_NAME)
        assertEquals(TEST_NAME, player.getName())
    }

    void testIsStarts() {
        assertEquals(false, player.getStarts())
    }

    void testSetStarts() {
        player.setStarts(true)
        assertTrue(player.getStarts())
    }

    void testDrawTile() {
        ArrayList<Tile> pool = new ArrayList<Tile>()
        pool.add(new Tile(1,2,3))
        player.drawTile(pool,1)

    }

    void testHighestValueTile() {
        Tile highest = player.highestValueTile()
        assertEquals(fiveFivesTile,highest)
    }

    void testToString() {
    }

    void testPlayATile() {
        player.setStarts(true)
        assertEquals(0,board.count())
        player.playATile(board,playedTiles,availableFaces)
        assertEquals(1,board.count())
    }
}
