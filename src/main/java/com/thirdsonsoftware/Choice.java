package com.thirdsonsoftware;

public class Choice {

    // Which tile?
    Tile tile ;

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
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

}
