package com.thirdsonsoftware

class RoundTest extends GroovyTestCase {

    public static final int DEFAULT_NUMBER_OF_PLAYERS = 3

    public static final String EMPTY_TILE_LIST = "[]"
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
    public static final String DEFAULT_PLAYED_TILES = " Played Tiles (0):\n  [<empty>]\n"
    public static final String DEFAULT_EMPTY_FACES = " Pieces on Board with Empty Faces (0):\n  [<empty>]\n"
    public static final String DEFAULT_PLAYER_DISPLAY = " Players (3):\n" +
            "  Name: Andy\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n" +
            "  Name: Brenda\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n" +
            "  Name: Charlese\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n  [<empty>]\n"

    Round round

    Player playerA = new Player('Andy')
    Player playerB = new Player('Brenda')
    Player playerC = new Player('Charlese')

    List<Tile> aListOfTiles = new ArrayList<Tile>()

    Tile tileA = new Tile(0,1,2)
    Tile tileB = new Tile(1,2,3)
    Tile tileC = new Tile(2,3,4)

    Tile TILE123 = new Tile(1,2,3)
    Tile TILE131 = new Tile(1,3,1)

    ArrayList<Player> players

    ArrayList<Tile> myTiles

    void setUp() {
        super.setUp()

        players = new ArrayList<Player>()
        players.add(playerA)
        players.add(playerB)
        players.add(playerC)

        round = new Round(0,players)

        myTiles = new ArrayList<Tile>()
        myTiles.add(tileA)
        myTiles.add(tileB)
        myTiles.add(tileC)
    }

    void tearDown() {
    }

    void testPlay() {
        round.play()
    }

    void testGenerateTiles() {
        String strTilesExpected = EXPECTED_UNSORTED_TILE_ARRAYLIST
        String strTilesActual = round.getTiles()
        assertEquals(strTilesExpected,strTilesActual)
    }

    /**
     * This is expecting the unshuffled set of tiles.  The toString() method
     *   converts the tile ArrayList to a bracketed String.
     */
    void testGetTiles() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, round.getTiles().toString())
    }

    void testSetTiles() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, round.getTiles().toString())
        round.setTiles(aListOfTiles)
        assertEquals(aListOfTiles,round.getTiles())
    }

    void testSetPiecesPlayed() {
        assertEquals(EMPTY_TILE_LIST,round.getPiecesPlayed().toString())
        aListOfTiles.add(TILE123)
        aListOfTiles.add(TILE131)
        round.setPiecesPlayed(aListOfTiles)
        assertEquals("[1-2-3, 1-3-1]",round.getPiecesPlayed().toString())
    }

    void testGetPiecesOnBoardWithEmptyFaces() {
        assertEquals(EMPTY_TILE_LIST,round.getPiecesOnBoardWithEmptyFaces().toString())
    }

    void testSetPiecesOnBoardWithEmptyFaces() {
        assertEquals(EMPTY_TILE_LIST,round.getPiecesOnBoardWithEmptyFaces().toString())
        round.setPiecesOnBoardWithEmptyFaces(aListOfTiles)
        assertEquals(aListOfTiles,round.getPiecesOnBoardWithEmptyFaces())
    }

    void testGetBoard() {
        assertEquals(round.board,round.getBoard())
    }

    void testGetPiecesPlayed() {
        assertEquals("[]",round.getPiecesPlayed().toString())
    }

    void testGetNumDraws() {
        assertEquals(round.UP_TO_FOUR_PLAYER_DRAWS, round.getNumDraws())
    }

    void testSetNumDraws() {
        assertEquals(round.UP_TO_FOUR_PLAYER_DRAWS, round.getNumDraws())
        round.setNumDraws(2)
        assertEquals(round.TWO_PLAYER_DRAWS, round.getNumDraws())
    }

    void testDrawTiles() {
    }

    void testShuffleTilePool() {
        assertEquals(EXPECTED_UNSORTED_TILE_ARRAYLIST, round.getTiles().toString())
        round.shuffleTilePool();
        assertNotSame(EXPECTED_UNSORTED_TILE_ARRAYLIST, round.getTiles().toString())
    }

    /**
     * Player A will always start with an unsorted tile list since
     */
    void testWhoIsFirst() {

        // Three players get 7 tiles each
        assertEquals(round.getNumDraws(),7)

        // Pretend to setup the players in a well known way...
        round.drawTiles()

        Player pIsFirst = round.whoIsFirst()
        assertEquals(pIsFirst, round.getPlayers()[0])

        boolean starts = round.getPlayers()[0].getStarts()
        assertTrue(starts)
        starts = round.getPlayers()[1].getStarts()
        assertFalse(starts)
        starts = round.getPlayers()[2].getStarts()
        assertFalse(starts)
    }

    void testDisplayTiles() {
        assertEquals("Test (56):\n  "+ EXPECTED_UNSORTED_TILE_ARRAYLIST+"\n",round.displayTiles(false,"Test",round.getTiles()))
    }

    void testDisplayTilePool() {
        assertEquals(" Tile Pool (56):\n" + EXPECTED_UNSORTED_TILE_IMAGE_ARRAYLIST + "\n",round.displayTilePool())
    }

    void testDisplayPlayedTiles() {
        assertEquals(DEFAULT_PLAYED_TILES, round.displayPlayedTiles())
    }

    void testDisplayTilesWithFacesRemaining() {
        assertEquals(DEFAULT_EMPTY_FACES, round.displayTilesWithFacesRemaining())
    }

    void testDisplayPlayers() {
        assertEquals(DEFAULT_PLAYER_DISPLAY,round.displayPlayers())
    }

    void testToString() {
        round.getBoard().findBoardMinMax(true)
        String strExpectedGame = \
            "Round 0:\n" +
            " Players (3):\n" +
            "  Name: Andy\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n" +
            "  [<empty>]\n" +
            "  Name: Brenda\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n" +
            "  [<empty>]\n" +
            "  Name: Charlese\n" +
            "    Starts: no\n" +
            "    Score: 0\n" +
            "    Hand (0):\n" +
            "  [<empty>]\n" +
            " Tile Pool (56):\n" +
            "     ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^       ^    \n" +
            "    /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /0\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /1\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /2\\     /3\\     /3\\     /3\\     /3\\     /3\\     /3\\     /4\\     /4\\     /5\\     /5\\   \n" +
            "   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\   /   \\  \n" +
            "  /0   0\\ /1   0\\ /2   0\\ /3   0\\ /4   0\\ /5   0\\ /1   1\\ /2   1\\ /3   1\\ /4   1\\ /5   1\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /1   1\\ /2   1\\ /3   1\\ /4   1\\ /5   1\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /2   2\\ /3   2\\ /4   2\\ /5   2\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /3   3\\ /4   3\\ /5   3\\ /4   4\\ /5   4\\ /5   5\\ /4   4\\ /5   4\\ /4   5\\ /5   5\\ \n" +
            "  --  1-- --  2-- --  3-- --  4-- --  5-- --  6-- --  7-- --  8-- --  9-- -- 10-- -- 11-- -- 12-- -- 13-- -- 14-- -- 15-- -- 16-- -- 17-- -- 18-- -- 19-- -- 20-- -- 21-- -- 22-- -- 23-- -- 24-- -- 25-- -- 26-- -- 27-- -- 28-- -- 29-- -- 30-- -- 31-- -- 32-- -- 33-- -- 34-- -- 35-- -- 36-- -- 37-- -- 38-- -- 39-- -- 40-- -- 41-- -- 42-- -- 43-- -- 44-- -- 45-- -- 46-- -- 47-- -- 48-- -- 49-- -- 50-- -- 51-- -- 52-- -- 53-- -- 54-- -- 55-- -- 56-- \n" +
            " Played Tiles (0):\n" +
            "  [<empty>]\n" +
            "Board:\n" +
            "  Played Piece Count:0\n" +
            "  Boundaries:(112,0,112,0)\n" +
            " Pieces on Board with Empty Faces (0):\n" +
            "  [<empty>]\n" ;
        String strActualGame = round.toString()
        assertEquals(strExpectedGame,strActualGame)
    }
}
