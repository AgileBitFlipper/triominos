package com.thirdsonsoftware;

public class Choice {

    // Which tile?
    Tile t ;

    // Where?
    int row ;
    int col ;

    public Choice(Tile t, int row, int col) {
        this.t = t;
        this.row = row;
        this.col = col;
    }

    public Tile getTile() {
        return t;
    }

    public void setTile(Tile t) {
        this.t = t;
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
