package com.thirdsonsoftware;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomPlayerSerializer extends StdSerializer<Player> {

    public CustomPlayerSerializer() {
        this(null);
    }

    public CustomPlayerSerializer(Class<Player> t) {
        super(t);
    }

    @Override
    public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializer) {
        try {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField("name", player.getName()) ;
            jsonGenerator.writeNumberField("wonAGameCount", player.getWonAGameCount()) ;
            jsonGenerator.writeNumberField("score", player.getScore());
            jsonGenerator.writeBooleanField("starts",player.getStarts());
            jsonGenerator.writeFieldName("startingTile");
            jsonGenerator.writeObject(player.getStartingTile());

            jsonGenerator.writeEndObject();
        } catch( IOException ioe ) {
            Log.Error("  IOException serializing the Player object: " + ioe.getMessage());
        }
    }
}
