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

    public static final int DEFAULT_NUMBER_OF_PLAYERS = 3
    public static final String PLAYER_A_NAME = "Player A"
    public static final String PLAYER_B_NAME = "Player B"
    public static final String PLAYER_C_NAME = "Player C"
    public static final String EXPECTED_UNSORTED_TILE_ARRAYLIST = "[0-0-0, 0-0-1, 0-0-2, 0-0-3, 0-0-4, 0-0-5, 0-1-1, 0-1-2," +
            " 0-1-3, 0-1-4, 0-1-5, 0-2-2, 0-2-3, 0-2-4, 0-2-5, 0-3-3, 0-3-4," +
            " 0-3-5, 0-4-4, 0-4-5, 0-5-5, 1-1-1, 1-1-2, 1-1-3, 1-1-4, 1-1-5," +
            " 1-2-2, 1-2-3, 1-2-4, 1-2-5, 1-3-3, 1-3-4, 1-3-5, 1-4-4, 1-4-5," +
            " 1-5-5, 2-2-2, 2-2-3, 2-2-4, 2-2-5, 2-3-3, 2-3-4, 2-3-5, 2-4-4," +
            " 2-4-5, 2-5-5, 3-3-3, 3-3-4, 3-3-5, 3-4-4, 3-4-5, 3-5-5, 4-4-4," +
            " 4-4-5, 5-5-4, 5-5-5]"
    public static final String EXPECTED_UNSORTED_TILE_IMAGE_ARRAYLIST = "     ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^    \n" +
            "    /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /3\\     /3\\     /3\\     /3\\     /3\\     /3\\     /4\\     /4\\     /5\\     /5\\   \n" +
            "   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\  \n" +
            "  /0   0\\ /1   0\\ /2   0\\ /3   0\\ /4   0\\ /5   0\\ /1   1\\ /2   1\\ /3   1\\ /4   1\\ /5   1\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /1   1\\ /2   1\\ /3   1\\ /4   1\\ /5   1\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /4   4\\ /5   4\\ /4   5\\ /5   5\\ \n" +
            "  --  1-- --  2-- --  3-- --  4-- --  5-- --  6-- --  7-- --  8-- --  9-- -- 10-- -- 11-- -- 12-- -- 13-- -- 14-- -- 15-- -- 16-- -- 17-- -- 18-- -- 19-- -- 20-- -- 21-- -- 22-- -- 23-- -- 24-- -- 25-- -- 26-- -- 27-- -- 28-- -- 29-- -- 30-- -- 31-- -- 32-- -- 33-- -- 34-- -- 35-- -- 36-- -- 37-- -- 38-- -- 39-- -- 40-- -- 41-- -- 42-- -- 43-- -- 44-- -- 45-- -- 46-- -- 47-- -- 48-- -- 49-- -- 50-- -- 51-- -- 52-- -- 53-- -- 54-- -- 55-- -- 56-- "
    public static final String EMPTY_TILE_LIST = "[]"
    public static final String UNSORTED_PLAYER_ONE_TRAY = "[0-0-0, 0-0-3, 0-1-1, 0-1-4, 0-2-3, 0-3-3, 0-4-4]"
    public static final String UNSORTED_PLAYER_TWO_TRAY = "[0-0-1, 0-0-4, 0-1-2, 0-1-5, 0-2-4, 0-3-4, 0-4-5]"
    public static final String UNSORTED_PLAYER_THREE_TRAY = "[0-0-2, 0-0-5, 0-1-3, 0-2-2, 0-2-5, 0-3-5, 0-5-5]"
    public static final String DEFAULT_PLAYED_TILES = "Played Tiles (0):\n  [<empty>]\n"
    public static final String DEFAULT_EMPTY_FACES = "Pieces on Board with Empty Faces (0):\n  [<empty>]\n"
    public static final String DEFAULT_PLAYER_DISPLAY = "Players (3):\n" +
            "  Name: Player A\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n" +
            "  Name: Player B\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n" +
            "  Name: Player C\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n"

    List<Tile> aListOfTiles

    public static final Tile TILE123 = new Tile(1,2,3)
    public static final Tile TILE131 = new Tile(1,3,1)

    Game game

    void setUp() {
        super.setUp()
        game = new Game(DEFAULT_NUMBER_OF_PLAYERS)

        aListOfTiles = new ArrayList<Tile>()
        aListOfTiles.add(TILE123)
    }

    void tearDown() {
    }

    void testToString() {
        game.board.findBoardMinMax(true)
        String strExpectedGame = "Game:\n Tile Pool (56):\n" +
                EXPECTED_UNSORTED_TILE_IMAGE_ARRAYLIST + "\n" +
                " Players (3):\n" +
                "  Name: Player A\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand (0):\n  [<empty>]\n" +
                "  Name: Player B\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand (0):\n  [<empty>]\n" +
                "  Name: Player C\n" +
                "    Starts: no\n" +
                "    Score: 0\n" +
                "    Hand (0):\n  [<empty>]\n" +
                " Played Tiles (0):\n" +
                "  [<empty>]\n" +
                " Board:\n" +
                "  Played Piece Count:0\n" +
                "  Boundaries:(112,0,112,0)\n" +
                " Pieces on Board with Empty Faces (0):\n" +
                "  [<empty>]\n"
        String strActualGame = game.toString()
        assertEquals(strExpectedGame,strActualGame)
    }

    /**
     * Player A will always start with an unsorted tile list since
     */
    void testWhoIsFirst() {

        // Three players get 7 tiles each
        assertEquals(game.getNumDraws(),7)

        // Pretend to setup the players in a well known way...
        game.drawTiles()

        Player pIsFirst = game.whoIsFirst()
        assertEquals(pIsFirst, game.getPlayer(0))

        boolean starts = game.getPlayer(0).getStarts()
        assertTrue(starts)
        starts = game.getPlayer(1).getStarts()
        assertFalse(starts)
        starts = game.getPlayer(2).getStarts()
        assertFalse(starts)
    }

    void testPlayerPicks() {

        // Since we didn't shuffle, we can predict the tile trays...
        game.drawTiles()

        String strPlayer0 = UNSORTED_PLAYER_ONE_TRAY
        String strPlayer1 = UNSORTED_PLAYER_TWO_TRAY
        String strPlayer2 = UNSORTED_PLAYER_THREE_TRAY

        assertEquals( strPlayer0, game.getPlayers()[0].tray.toString() )
        assertEquals( strPlayer1, game.getPlayers()[1].tray.toString() )
        assertEquals( strPlayer2, game.getPlayers()[2].tray.toString() )
    }

    void testGenerateTiles() {
        String strTilesExpected = EXPECTED_UNSORTED_TILE_ARRAYLIST
        String strTilesActual = game.getTiles()
        assertEquals(strTilesExpected,strTilesActual)
    }

    void testGetPlayer() {
        Player player = game.getPlayer(0)
        assertEquals(PLAYER_A_NAME, player.getName())
        player = game.getPlayer(2)
        assertEquals(PLAYER_C_NAME,player.getName())
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

    /**
     * This is expecting the unshuffled set of tiles.  The toString() method
     *   converts the tile ArrayList to a bracketed String.
     */
    void testGetTiles() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, game.getTiles().toString())
    }

    void testSetTiles() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, game.getTiles().toString())
        game.setTiles(aListOfTiles)
        assertEquals(aListOfTiles,game.getTiles())
    }

    void testGetPiecesPlayed() {
        assertEquals(EMPTY_TILE_LIST,game.getPiecesPlayed().toString())
    }

    void testSetPiecesPlayed() {
        assertEquals(EMPTY_TILE_LIST,game.getPiecesPlayed().toString())
        aListOfTiles.add(TILE131)
        game.setPiecesPlayed(aListOfTiles)
        assertEquals("[1-2-3, 1-3-1]",game.getPiecesPlayed().toString())
    }

    void testGetPiecesOnBoardWithEmptyFaces() {
        assertEquals(EMPTY_TILE_LIST,game.getPiecesOnBoardWithEmptyFaces().toString())
    }

    void testSetPiecesOnBoardWithEmptyFaces() {
        assertEquals(EMPTY_TILE_LIST,game.getPiecesOnBoardWithEmptyFaces().toString())
        game.setPiecesOnBoardWithEmptyFaces(aListOfTiles)
        assertEquals(aListOfTiles,game.getPiecesOnBoardWithEmptyFaces())
    }

    void testGetBoard() {
        assertEquals(game.board,game.getBoard())
    }

    void testPlay() {
    }

    void testDrawTiles() {
        assertEquals(EMPTY_TILE_LIST, game.getPlayer(0).tray.toString())
        game.drawTiles();
        assertEquals(UNSORTED_PLAYER_ONE_TRAY, game.getPlayer(0).tray.toString())
        assertEquals(UNSORTED_PLAYER_TWO_TRAY, game.getPlayer(1).tray.toString())
        assertEquals(UNSORTED_PLAYER_THREE_TRAY, game.getPlayer(2).tray.toString())
    }

    void testShuffleTilePool() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, game.getTiles().toString())
        game.shuffleTilePool();
        assertNotSame(EXPECTED_UNSORTED_TILE_ARRAYLIST, game.getTiles().toString())
    }

    void testDisplayTiles() {
        assertEquals("Test (56):\n  "+ EXPECTED_UNSORTED_TILE_ARRAYLIST+"\n",game.displayTiles(false,"Test",game.getTiles()))
    }

    void testDisplayTilePool() {
        assertEquals("Tile Pool (56):\n" + EXPECTED_UNSORTED_TILE_IMAGE_ARRAYLIST + "\n",game.displayTilePool())
    }

    void testDisplayPlayedTiles() {
        assertEquals(DEFAULT_PLAYED_TILES, game.displayPlayedTiles())
    }

    void testDisplayTilesWithFacesRemaining() {
        assertEquals(DEFAULT_EMPTY_FACES, game.displayTilesWithFacesRemaining())
    }

    void testDisplayPlayers() {
        assertEquals(DEFAULT_PLAYER_DISPLAY,game.displayPlayers())
    }

    void testGetNumPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
    }

    void testSetNumPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
        game.setNumPlayers(2)
        assertFalse(DEFAULT_NUMBER_OF_PLAYERS == game.getNumPlayers())
    }
}
