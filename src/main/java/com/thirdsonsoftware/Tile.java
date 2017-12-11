/*
 * Copyright (c) 2017 Third Son Software, LLC
 * (http://thirdsonsoftware.com/) and others. All rights reserved.
 * Created by Andrew B. Montcrieff on 11/30/17 5:08 PM.
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
 * Last modified: 11/30/17 4:59 PM
 */

package com.thirdsonsoftware;

import java.io.Serializable;

/** Represents the orientation of a tile.
 */
enum Orientation {
    UP,
    DOWN
}

/** Represents a tile in the game.
 * Tiles are constructed in a
 *   clockwise fashion.
 *
 *      A
 *     / \
 *    /   \
 *   C-----B
 *
 * @author Andrew B. Montcrieff
 * @author www.ThirdSonSoftware.com
 * @version 0.1
 * @since 0.0
 */
public class Tile implements Comparable, Serializable {

    private int id ;                    // Unique tile id

    private int cornerA ;               // Reference corner A
    private int cornerB ;               // Reference corner B
    private int cornerC ;               // Reference corner C

    private int row ;                   // Where on the board is this tile?
    private int col ;                   // Where on the board is this tile?

    private int value ;                 // Value of the tile (corner adds)
    private int rotation ;              // 0, 120, or 240

    private Boolean placed ;            // Has this piece been placed?

    private Boolean tray ;              // Is this piece in a tray?

    private Orientation orientation ;   // UP and DOWN

    private Player playedBy ;           // Which player played this tile

    public Tile(int cornerA, int cornerB, int cornerC) {

        setCornerA( cornerA );
        setCornerB( cornerB );
        setCornerC( cornerC );

        setRotation(0) ;
        setOrientation(Orientation.DOWN);

        setValue( cornerA + cornerB + cornerC );

        setInTray(false) ;
        setPlayer(null) ;
        setPlaced(false) ;
    }

    public String toString() {
        return String.format( "%d-%d-%d", cornerA, cornerB, cornerC );
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    private void setCornerA(int cornerA) {
        this.cornerA = cornerA;
    }
    private void setCornerB(int cornerB) {
        this.cornerB = cornerB;
    }
    public void setCornerC(int cornerC) {
        this.cornerC = cornerC;
    }

    public int getCornerA() {
        return cornerA;
    }
    public int getCornerB() { return cornerB; }
    public int getCornerC() { return cornerC; }

    public int getRotation() {
        return rotation;
    }
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public Orientation getOrientation() {
        return orientation;
    }
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getValue() { return value; }
    public void setValue(int value) {
        this.value = value;
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

    public boolean isTriplet() {
        return ( ( getCornerA() == getCornerB() ) && ( getCornerB() == getCornerC() ) );
    }

    public void rotate( int r ) {
        rotation = r ;
    }

    /**
     * Determines if a Tile has been placed on the board or not.
     * @return - true if tile tile has been played, false otherwise
     */
    public Boolean getPlaced() {
        return placed;
    }

    /**
     * Sets if a tile has been placed on the board.
     * @param placed - true if placed on the board, false otherwise
     */
    public void setPlaced(Boolean placed) {
        this.placed = placed;
    }

    /**
     * Is the tile in a tray?
     * @return - true if the tile is in a player's tray, false otherwise
     */
    public Boolean getInTray() {
        return tray;
    }

    /**
     * Mark the tile as in someone's tray.
     * @param tray - true if in a player's tray, false otherwise
     */
    public void setInTray(Boolean tray) {
        this.tray = tray;
    }

    /**
     * Returns the left face of the Tile, always oriented outward from
     *   the center of the tile, and taking into account both orientation
     *   and rotation.
     * @return Face - the left face of the Tile in place
     */
    public Face getLeftFace() {
        if ( orientation == Orientation.DOWN ) {
            return new Face(getMiddleCorner(), getLeftCorner());
        } else {
            return new Face(getLeftCorner(), getMiddleCorner());
        }
    }

    /**
     * Returns the right face of the Tile, always oriented outward from
     *   the center of the tile, and taking into account both orientation
     *   and rotation.
     * @return Face - the right face of the Tile in place
     */
    public Face getRightFace() {
        if ( orientation == Orientation.DOWN ) {
            return new Face(getRightCorner(), getMiddleCorner());
        } else {
            return new Face(getMiddleCorner(), getRightCorner());
        }
    }

    /**
     * Returns the middle face of the Tile, always oriented outward from
     *   the center of the tile, and taking into account both orientation
     *   and rotation.
     * @return Face - the middle face of the Tile in place
     */
    public Face getMiddleFace() {
        if ( orientation == Orientation.DOWN ) {
            return new Face(getLeftCorner(), getRightCorner());
        } else {
            return new Face(getRightCorner(), getLeftCorner());
        }
    }

    /**
     * Adds the player (owner) to the tile for reconciliation later.
     */
    public void setPlayer(Player player) {
        this.playedBy = player ;
    }

    /**
     * Returns the player assigned to this tile.
     * @return Player - the player that played the tile or owns it in their tray.
     */
    public Player getPlayer() {
        return playedBy ;
    }

    /**
     * Draws the tile in the row of Strings provided.
     * @param solo - If true, the tile is by itself.  If false, it will be inline with others.
     * @param row - Array of Strings to draw the tile in
     */
    public void draw( Boolean solo, String[] row ) {

        char playerIndexChar ;

        if ( getPlayer() != null && getPlayer().getName() != null )
            playerIndexChar = getPlayer().getName().charAt(getPlayer().getName().length()-1) ;
        else
            playerIndexChar = '?';

        char ch ;

        if ( getInTray() ) {
            ch = 'T';
        } else if ( getPlaced() ) {
            ch = 'P';
        } else {
            ch = ' ';
        }

        // Column
        if ( getOrientation()==Orientation.DOWN ) {

            row[0] += String.format("%s--%3d--%s",   (solo) ? "" : " "  , getId()                              , (solo) ? " "    :" ");
            row[1] += String.format("\\%d %c %d/%s", getLeftCorner()    , ch                , getRightCorner() , (solo) ? "  "   :"");
            row[2] += String.format("%s\\ %c /%s",   (solo) ? " " : ""  , playerIndexChar                      , (solo) ? "   "  :"");
            row[3] += String.format("%s\\%d/%s",     (solo) ? "  " : "" , getMiddleCorner()                    , (solo) ? "    " :"");
            row[4] += String.format("%sv%s",         (solo) ? "   " : ""                                       , (solo) ? "     ":"");

        } else {

            row[0] += String.format("%s^%s",         (solo) ? "   " :""                                   , (solo) ? "    ":"");
            row[1] += String.format("%s/%d\\%s",     (solo) ? "  "  :""  , getMiddleCorner()              , (solo) ? "   " :"");
            row[2] += String.format("%s/ %c \\%s",   (solo) ? " "   :""  , playerIndexChar                , (solo) ? "  "  :"") ;
            row[3] += String.format("/%d %c %d\\%s", getLeftCorner()     , ch      , getRightCorner()     , (solo) ? " "   :"");
            row[4] += String.format("%s--%3d--%s%s", (solo) ? ""    :" " , getId() , (solo) ? "" : " "    , (solo) ? " "   :"");
        }
    }

    public int getLeftCorner() {
        switch (getRotation()) {
            case 0:
                if (getOrientation() == Orientation.UP) {
                    return getCornerC();
                } else {
                    return getCornerB();
                }
            case 120:
                if (getOrientation() == Orientation.UP) {
                    return getCornerB();
                } else {
                    return getCornerA();
                }
            default:
                if (getOrientation() == Orientation.UP) {
                    return getCornerA();
                } else {
                    return getCornerC();
                }
        }
    }

    public int getRightCorner() {
        switch (getRotation()) {
            case 0:
                if (getOrientation() == Orientation.UP) {
                    return getCornerB();
                } else {
                    return getCornerC();
                }
            case 120:
                if (getOrientation() == Orientation.UP) {
                    return getCornerA();
                } else {
                    return getCornerB();
                }
            default:
                if (getOrientation() == Orientation.UP) {
                    return getCornerC();
                } else {
                    return getCornerA();
                }
        }
    }

    public int getMiddleCorner() {
        switch (getRotation()) {
            case 0:
                if (getOrientation() == Orientation.UP) {
                    return getCornerA();
                } else {
                    return getCornerA();
                }
            case 120:
                if (getOrientation() == Orientation.UP) {
                    return getCornerC();
                } else {
                    return getCornerC();
                }
            default:
                if (getOrientation() == Orientation.UP) {
                    return getCornerB();
                } else {
                    return getCornerB();
                }
        }
    }

    @Override
    public int compareTo(Object o) {
        Tile t = (Tile)o;
        if ( getCornerA() < t.getCornerA() )
            return -1;
        else if ( getCornerA() > t.getCornerA() )
            return 1;

        if ( getCornerB() < t.getCornerB() )
            return -1;
        else if ( getCornerB() > t.getCornerB() )
            return 1;

        if ( getCornerC() < t.getCornerC() )
            return -1;
        else if ( getCornerC() > t.getCornerC() )
            return 1;

        return 0;
    }

}
