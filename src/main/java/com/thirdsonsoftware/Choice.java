package com.thirdsonsoftware;

public class Choice {

    // Which tile?
    Tile tile ;

    // If this choice is taken, what would the score be?
    private int score ;

    // Where?
    private int row ;
    private int col ;

    // How?
    private Orientation orientation ;

    private int rotation ;

    public Choice(Tile tile, int row, int col, Orientation orientation, int rotation) {
        this.tile = tile;
        this.row = row;
        this.col = col;
        this.orientation = orientation;
        this.rotation = rotation;
        this.score = 0 ;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Orientation getOrientation() {
        return this.orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getRotation() {
        return this.rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getScore() { return this.score ; }

    public void setScore(int s) { this.score = s ; }

}
