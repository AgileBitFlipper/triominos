package com.thirdsonsoftware;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomEventSerializer extends StdSerializer<Event> {

    /**
     * Generated serial version identification.
     */
    private static final long serialVersionUID = 8930014496347149359L;

    public CustomEventSerializer() {
        this(null);
    }

    public CustomEventSerializer(Class<Event> t) {
        super(t);
    }

    @Override
    public void serialize(Event event, JsonGenerator jsonGenerator, SerializerProvider serializer) {
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("eventDateTime", event.eventDateTime.toString());
            jsonGenerator.writeStringField("type", event.type.toString());
            jsonGenerator.writeFieldName("Player");
            jsonGenerator.writeObject(event.player);
            jsonGenerator.writeFieldName("Tile");
            jsonGenerator.writeObject(event.tile);
            jsonGenerator.writeNumberField("row", event.row);
            jsonGenerator.writeNumberField("col", event.col);
            jsonGenerator.writeNumberField("score", event.score);
            jsonGenerator.writeNumberField("startBonus", event.startBonus);
            jsonGenerator.writeBooleanField("startingMove", event.startingMove);
            jsonGenerator.writeBooleanField("completedAHexagon", event.completedAHexagon);
            jsonGenerator.writeBooleanField("completedABridge", event.completedABridge);
            jsonGenerator.writeBooleanField("endOfRound", event.endOfRound);
            jsonGenerator.writeBooleanField("endOfGame", event.endOfGame);
            jsonGenerator.writeNumberField("round", event.round);
            jsonGenerator.writeNumberField("game", event.game);
            jsonGenerator.writeEndObject();

        } catch (IOException ioe) {

            Log.Error("  IOException serializing the Event object: " + ioe.getMessage());
        
        }
    }
}
