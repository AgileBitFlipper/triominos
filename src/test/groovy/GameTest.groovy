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
    public static final String EMPTY_TILE_LIST = "[]"
    public static final String UNSORTED_PLAYER_ONE_TRAY = "[0-0-0, 0-0-3, 0-1-1, 0-1-4, 0-2-3, 0-3-3, 0-4-4]"
    public static final String UNSORTED_PLAYER_TWO_TRAY = "[0-0-1, 0-0-4, 0-1-2, 0-1-5, 0-2-4, 0-3-4, 0-4-5]"
    public static final String UNSORTED_PLAYER_THREE_TRAY = "[0-0-2, 0-0-5, 0-1-3, 0-2-2, 0-2-5, 0-3-5, 0-5-5]"
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
        String strExpectedGame = "\n\nGame Results:\n  Rounds:\n"
        String strActualGame = game.toString()
        assertEquals(strExpectedGame,strActualGame)
    }

    void testGetPlayer() {
        Player player = game.getPlayer(0)
        assertEquals(PLAYER_A_NAME, player.getName())
        player = game.getPlayer(2)
        assertEquals(PLAYER_C_NAME,player.getName())
    }

    void testGetPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
    }

    void testSetPlayers() {
        assertEquals(DEFAULT_NUMBER_OF_PLAYERS,game.getNumPlayers())
        game.setNumPlayers(2)
        assertEquals(2,game.getNumPlayers())
    }

    void testPlay() {
    }

}
