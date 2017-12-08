package com.thirdsonsoftware;

public class Choice {

    // Which tile?
    Tile tile ;

    // Where?
    int row ;
    int col ;

    public Choice(Tile tile, int row, int col) {
        this.tile = tile;
        this.row = row;
        this.col = col;
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
}
