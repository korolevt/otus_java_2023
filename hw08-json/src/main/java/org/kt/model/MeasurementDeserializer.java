package org.kt.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class MeasurementDeserializer extends JsonDeserializer<Measurement> {
    @Override
    public Measurement deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        final String name = node.get("name").asText();
        final double value = node.get("value").asDouble();

        return new Measurement(name, value);
    }
}

