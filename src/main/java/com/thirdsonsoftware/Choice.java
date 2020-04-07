package com.thirdsonsoftware;

/**
 * The Choice class helps us decide what the board will look like
 *   and the potential score if a Tile is placed.  This also allows
 *   us to keep track of the myriad possible plays we can take
 *   per turn.  That way, we can choose the one with the greatest
 *   value as determined by its placement on the board.
 */
public class Choice {

    // Which tile?
    Tile tile;

    boolean testForFitOnly;

    // If this choice is taken, what would the score be?
    private int score;

    // Where?
    private int row;
    private int col;

    // How?
    private Orientation orientation;

    private int rotation;

    /**
     * Constructs a single choice based on a tile, position, orientation,
     *   rotation and score.
     * Note:  Even though a Tile object can hold row, col, orientation and
     *   rotation, it can't hold more than one set of these values.  That's
     *   why a choice was created.  The choice holds the tile reference and
     *   how that tile should be placed on the board for this particular
     *   instance.
     * @param tile - the tile to place
     * @param row - the row to place it at
     * @param col - the column to place it at
     * @param orientation - the orientation to use
     * @param rotation - the rotatino to use
     */
    public Choice(Tile tile, int row, int col, Orientation orientation, int rotation) {
        this.tile = tile;
        this.row = row;
        this.col = col;
        this.orientation = orientation;
        this.rotation = rotation;
        this.score = 0;
    }

    /**
     * Get the Choice's tile reference.
     * @return (Tile)
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Set the tile reference.
     * @param tile - the Tile to set in the choice
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Getter for the testForFitOnly flag.
     * @return
     */
    public boolean isTestForFitOnly() {
        return testForFitOnly;
    }

    /**
     * Setter for the testForFitOnly flag.  If set, this means that
     *   this test is only to see if it fits, not that it is actually
     *   placing a Tile.
     * @param testForFitOnly - should we test for fit only
     */
    public void setTestForFitOnly(boolean testForFitOnly) {
        this.testForFitOnly = testForFitOnly;
    }

    /**
     * Get the row of the Choice.
     * @return
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Set the row of the Choice.
     * @param row - the Row to set the Choice to
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Get the column of the Choice.
     * @return
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Set the column of the Choice.
     * @param col - the Column to set the Choice to
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Get the orientation of the Choice.
     * @return
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * Set the orientation of the Choice.
     * @param orientation - the orientation to set the Choice to
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Get the rotation of the Choice.
     * @return
     */
    public int getRotation() {
        return this.rotation;
    }

    /**
     * Set the rotation of the Choice.
     * @param rotation - the Rotation to set the Choice to
     */
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    /**
     * Get the score for this Choice.
     * @return
     */
    public int getScore() { 
        return this.score; 
    }

    /**
     * Set the score for this Choice.
     * @param s - the score to set in this choice
     */
    public void setScore(int s) { 
        this.score = s; 
    }

}
