/*
 * Copyright (c) 2017 Third Son Software, LLC
 * (http://thirdsonsoftware.com/) and others. All rights reserved.
 * Created by Andrew B. Montcrieff on 11/30/17 5:17 PM.
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
 * Last modified: 11/30/17 5:17 PM
 */

package com.thirdsonsoftware

class GameTest extends GroovyTestCase {

    Game game

    void setUp() {
        super.setUp()
        game = new Game(3)
    }

    void tearDown() {
    }

    void testToString() {
        String strExpectedGame = "Game:\nTile Pool:\n" +
                "  0-0-0,\n  0-0-1,\n  0-0-2,\n  0-0-3,\n" +
                "  0-0-4,\n  0-0-5,\n  0-1-1,\n  0-1-2,\n" +
                "  0-1-3,\n  0-1-4,\n  0-1-5,\n  0-2-2,\n" +
                "  0-2-3,\n  0-2-4,\n  0-2-5,\n  0-3-3,\n" +
                "  0-3-4,\n  0-3-5,\n  0-4-4,\n  0-4-5,\n  0-5-5,\n" +
                "  1-1-1,\n  1-1-2,\n  1-1-3,\n  1-1-4,\n  1-1-5,\n" +
                "  1-2-2,\n  1-2-3,\n  1-2-4,\n  1-2-5,\n  1-3-3,\n" +
                "  1-3-4,\n  1-3-5,\n  1-4-4,\n  1-4-5,\n  1-5-5,\n" +
                "  2-2-2,\n  2-2-3,\n  2-2-4,\n  2-2-5,\n  2-3-3,\n" +
                "  2-3-4,\n  2-3-5,\n  2-4-4,\n  2-4-5,\n  2-5-5,\n" +
                "  3-3-3,\n  3-3-4,\n  3-3-5,\n  3-4-4,\n  3-4-5,\n" +
                "  3-5-5,\n" +
                "  4-4-4,\n  4-4-5,\n" +
                "  5-5-4,\n  5-5-5\n" +
                "Players:\n" +
                "  Count:3\n" +
                "  Name: Player 1\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand: []\n" +
                "  Name: Player 2\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand: []\n" +
                "  Name: Player 3\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand: []\n" +
                "Board:\n" +
                "  Played Piece Count:0\n" +
                "  Boundaries:(0,112,0,112)\n"
        String strActualGame = game
        assertEquals(strExpectedGame,strActualGame)
    }

    void testWhoIsFirst() {

        // Three players get 7 tiles each
        assertEquals(game.getNumDraws(),7)

        // Pretend to setup the players in a well known way...
        game.setupPlayerTrays()

        Player pIsFirst = game.whoIsFirst()
        assertEquals(pIsFirst, game.getPlayers()[2])

        boolean starts = game.getPlayers()[0].getStarts()
        assertFalse(starts)
        starts = game.getPlayers()[1].getStarts()
        assertFalse(starts)
        starts = game.getPlayers()[2].getStarts()
        assertTrue(starts)

    }

    void testPlayerPicks() {

        // Since we didn't shuffle, we can predict the tile trays...
        game.setupPlayerTrays()

        String strPlayer0 = "[0-0-0, 0-0-3, 0-1-1, 0-1-4, 0-2-3, 0-3-3, 0-4-4]"
        String strPlayer1 = "[0-0-1, 0-0-4, 0-1-2, 0-1-5, 0-2-4, 0-3-4, 0-4-5]"
        String strPlayer2 = "[0-0-2, 0-0-5, 0-1-3, 0-2-2, 0-2-5, 0-3-5, 0-5-5]"

        assertEquals( strPlayer0, game.getPlayers()[0].tray.toString() )
        assertEquals( strPlayer1, game.getPlayers()[1].tray.toString() )
        assertEquals( strPlayer2, game.getPlayers()[2].tray.toString() )
    }

    void testGenerateTiles() {
        String strTilesExpected = "" +
                "[0-0-0, 0-0-1, 0-0-2, 0-0-3, 0-0-4, 0-0-5, 0-1-1, 0-1-2," +
                " 0-1-3, 0-1-4, 0-1-5, 0-2-2, 0-2-3, 0-2-4, 0-2-5, 0-3-3," +
                " 0-3-4, 0-3-5, 0-4-4, 0-4-5, 0-5-5," +
                " 1-1-1, 1-1-2, 1-1-3, 1-1-4, 1-1-5, 1-2-2, 1-2-3, 1-2-4," +
                " 1-2-5, 1-3-3, 1-3-4, 1-3-5, 1-4-4, 1-4-5, 1-5-5," +
                " 2-2-2, 2-2-3, 2-2-4, 2-2-5, 2-3-3, 2-3-4, 2-3-5, 2-4-4," +
                " 2-4-5, 2-5-5," +
                " 3-3-3, 3-3-4, 3-3-5, 3-4-4, 3-4-5, 3-5-5," +
                " 4-4-4, 4-4-5, 5-5-4, 5-5-5]"
        String strTilesActual = game.getTiles()
        assertEquals(strTilesExpected,strTilesActual)
    }
}
