package com.thirdsonsoftware

class RoundTest extends GroovyTestCase {

    Round round ;

    Player playerA = new Player('Andy')
    Player playerB = new Player('Brenda')
    Player playerC = new Player('Charlese')

    Tile tileA = new Tile(0,1,2)
    Tile tileB = new Tile(1,2,3)
    Tile tileC = new Tile(2,3,4)

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

    void testGetTiles() {
        String strTiles = round.getTiles()
        assertEquals(
                "[0-0-0, 0-0-1, 0-0-2, 0-0-3, 0-0-4, 0-0-5, 0-1-1, 0-1-2," +
                " 0-1-3, 0-1-4, 0-1-5, 0-2-2, 0-2-3, 0-2-4, 0-2-5, 0-3-3," +
                " 0-3-4, 0-3-5, 0-4-4, 0-4-5, 0-5-5, 1-1-1, 1-1-2, 1-1-3," +
                " 1-1-4, 1-1-5, 1-2-2, 1-2-3, 1-2-4, 1-2-5, 1-3-3, 1-3-4," +
                " 1-3-5, 1-4-4, 1-4-5, 1-5-5, 2-2-2, 2-2-3, 2-2-4, 2-2-5," +
                " 2-3-3, 2-3-4, 2-3-5, 2-4-4, 2-4-5, 2-5-5, 3-3-3, 3-3-4," +
                " 3-3-5, 3-4-4, 3-4-5, 3-5-5, 4-4-4, 4-4-5, 5-5-4, 5-5-5]",
                round.getTiles().toString())

    }

    void testSetTiles() {
        round.setTiles(myTiles)
        assertEquals(myTiles,round.getTiles())
        assertEquals(myTiles.get(0),tileA)
        assertEquals(myTiles.get(1),tileB)
    }

    void testGetPiecesPlayed() {
        assertEquals("[]",round.getPiecesPlayed().toString())
    }

    void testSetPiecesPlayed() {
        assertEquals("[]",round.getPiecesPlayed().toString())
        round.setPiecesPlayed(myTiles)
        assertEquals("[0-1-2, 1-2-3, 2-3-4]",round.getPiecesPlayed().toString())
    }

    void testGetPiecesOnBoardWithEmptyFaces() {
        assertEquals("[]",round.getPiecesOnBoardWithEmptyFaces().toString())
    }

    void testSetPiecesOnBoardWithEmptyFaces() {
        assertEquals("[]",round.getPiecesOnBoardWithEmptyFaces().toString())
        round.setPiecesOnBoardWithEmptyFaces(myTiles)
        assertEquals("[0-1-2, 1-2-3, 2-3-4]",round.getPiecesOnBoardWithEmptyFaces().toString())
    }

    void testGetBoard() {
        assertEquals("Board:\n" +
                "  Played Piece Count:0\n" +
                "  Boundaries:(112,0,112,0)\n",round.getBoard().toString())
    }

    void testGetPlayers() {
    }

    void testSetNumDraws() {
    }

    void testGetNumDraws() {
    }

    void testDrawTiles() {
    }

    void testShuffleTilePool() {
    }

    void testWhoIsFirst() {
    }

    void testGenerateTiles() {
    }

    void testDisplayTiles() {
    }

    void testDisplayTilePool() {
    }

    void testDisplayPlayedTiles() {
    }

    void testDisplayTilesWithFacesRemaining() {
    }

    void testToString() {
    }
}
