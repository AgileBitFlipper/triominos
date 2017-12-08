package com.thirdsonsoftware

class ChoiceTest extends GroovyTestCase {

    final static int DEFAULT_ROW = 56
    final static int DEFAULT_COL = 56

    Choice choice

    Tile tile123
    Tile tile555

    void setUp() {
        super.setUp()

        tile123 = new Tile(1,2,3)
        tile555 = new Tile(5,5,5)

        choice = new Choice( tile123, DEFAULT_ROW, DEFAULT_COL, Orientation.UP, 0)
    }

    void tearDown() {
    }

    void testGetTile() {
        assertEquals(tile123, choice.getTile())
    }

    void testSetTile() {
        assertEquals(tile123,choice.getTile())
        choice.setTile(tile555)
        assertEquals(tile555,choice.getTile())
    }

    void testGetRow() {
        assertEquals(DEFAULT_ROW,choice.getRow())
    }

    void testSetRow() {
        assertEquals(DEFAULT_ROW,choice.getRow())
        choice.setRow(0)
        assertEquals(0,choice.getRow())
    }

    void testGetCol() {
        assertEquals(DEFAULT_COL,choice.getCol())
    }

    void testSetCol() {
        assertEquals(DEFAULT_COL,choice.getCol())
        choice.setCol(0)
        assertEquals(0,choice.getCol())
    }
}
