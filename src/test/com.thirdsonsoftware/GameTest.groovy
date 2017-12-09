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

    int DEFAULT_NUMBER_OF_PLAYERS = 3


    Game game

    void setUp() {
        super.setUp()
        game = new Game(DEFAULT_NUMBER_OF_PLAYERS)
    }

    void tearDown() {
    }

    void testToString() {
        game.board.findBoardMinMax(true)
        String strExpectedGame = "Game:\nTile Pool (56):\n" +
                "  [0-0-0, 0-0-1, 0-0-2, 0-0-3, 0-0-4, 0-0-5, 0-1-1, 0-1-2, 0-1-3, 0-1-4, 0-1-5," +
                " 0-2-2, 0-2-3, 0-2-4, 0-2-5, 0-3-3, 0-3-4, 0-3-5, 0-4-4, 0-4-5, 0-5-5," +
                " 1-1-1, 1-1-2, 1-1-3, 1-1-4, 1-1-5, 1-2-2, 1-2-3, 1-2-4, 1-2-5, 1-3-3, 1-3-4, 1-3-5, 1-4-4, 1-4-5, 1-5-5," +
                " 2-2-2, 2-2-3, 2-2-4, 2-2-5, 2-3-3, 2-3-4, 2-3-5, 2-4-4, 2-4-5, 2-5-5," +
                " 3-3-3, 3-3-4, 3-3-5, 3-4-4, 3-4-5, 3-5-5, 4-4-4, 4-4-5, 5-5-4, 5-5-5]\n" +
                "Players (3):\n" +
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
                "Played Tiles (0):\n" +
                "  [<empty>]\n" +
                "Board:\n" +
                "  Played Piece Count:0\n" +
                "  Boundaries:(112,0,112,0)\n" +
                "Pieces on Board with Empty Faces (0):\n" +
                "  [<empty>]\n"
        String strActualGame = game.toString()
        assertEquals(strExpectedGame,strActualGame)
    }

    void testWhoIsFirst() {

        // Three players get 7 tiles each
        assertEquals(game.getNumDraws(),7)

        // Pretend to setup the players in a well known way...
        game.drawTiles()

        Player pIsFirst = game.whoIsFirst()
        assertEquals(pIsFirst, game.getPlayer(2))

        boolean starts = game.getPlayer(0).getStarts()
        assertFalse(starts)
        starts = game.getPlayer(1).getStarts()
        assertFalse(starts)
        starts = game.getPlayer(2).getStarts()
        assertTrue(starts)

    }

    void testPlayerPicks() {

        // Since we didn't shuffle, we can predict the tile trays...
        game.drawTiles()

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

    void testGetPlayer() {
        Player player = game.getPlayer(0)
        assertEquals("Player 1", player.getName())
        player = game.getPlayer(2)
        assertEquals("Player 3",player.getName())
    }

    void testGetNumDraws() {
        assertEquals(Game.UP_TO_FOUR_PLAYER_DRAWS, game.getNumDraws())
    }

    void testSetNumDraws() {
        assertEquals(Game.UP_TO_FOUR_PLAYER_DRAWS, game.getNumDraws())
        game.setNumDraws(Game.TWO_PLAYER_DRAWS)
        assertEquals(Game.TWO_PLAYER_DRAWS, game.getNumDraws())
    }

    void testGetPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
    }

    void testSetPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
        game.setNumPlayers(2)
        assertEquals(2,game.getNumPlayers())
    }

    void testGetTiles() {
    }

    void testSetTiles() {
    }

    void testGetPiecesPlayed() {
    }

    void testSetPiecesPlayed() {
    }

    void testGetPiecesOnBoardWithEmptyFaces() {
    }

    void testSetPiecesOnBoardWithEmptyFaces() {
    }

    void testGetBoard() {
    }

    void testSetupPlayerTrays() {
    }

    void testPlay() {
    }
}
