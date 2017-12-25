package com.thirdsonsoftware;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Represents the orientation of a tile.
 */
@JsonSerialize(using = CustomTileSerializer.class)
public enum Orientation {
    UP,
    DOWN
}

