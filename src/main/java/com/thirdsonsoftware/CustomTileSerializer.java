package com.thirdsonsoftware;

import static com.thirdsonsoftware.Tile.bUseColors;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomTileSerializer extends StdSerializer<Tile> {

    /**
     * Generated serial version IDentifier.
     */
    private static final long serialVersionUID = -7501752336754119463L;

    public CustomTileSerializer() {
        this(null);
    }

    public CustomTileSerializer(Class<Tile> t) {
        super(t);
    }

    @Override
    public void serialize(Tile tile, JsonGenerator jsonGenerator, SerializerProvider serializer) {
        try {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeNumberField("id", tile.getId());
            jsonGenerator.writeNumberField("cornerA", tile.getCornerA());
            jsonGenerator.writeNumberField("cornerB", tile.getCornerB());
            jsonGenerator.writeNumberField("cornerC", tile.getCornerC());
            jsonGenerator.writeNumberField("row", tile.getRow());
            jsonGenerator.writeNumberField("col", tile.getCol());
            jsonGenerator.writeNumberField("value", tile.getValue());
            jsonGenerator.writeNumberField("rotation", tile.getRotation());
            jsonGenerator.writeBooleanField("placed", tile.getPlaced());
            jsonGenerator.writeBooleanField("tray", tile.getInTray());
            jsonGenerator.writeStringField("orientation", tile.getOrientation().toString());
            // This causes a stack overflow when a Player prints a tile that prints a
            // player...
            // jsonGenerator.writeFieldName("Player");
            // jsonGenerator.writeObject(tile.getPlayer());
            jsonGenerator.writeBooleanField("bUseColors", bUseColors);

            jsonGenerator.writeEndObject();
        } catch (IOException ioe) {
            Log.Error("  IOException serializing the Player object: " + ioe.getMessage());
        }
    }
}
