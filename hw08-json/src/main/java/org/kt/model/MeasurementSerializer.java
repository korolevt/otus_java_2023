package org.kt.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class MeasurementSerializer extends StdSerializer<Measurement> {
    public MeasurementSerializer(Class<Measurement> t) {
        super(t);
    }

    @Override
    public void serialize(Measurement measurement, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("name", measurement.getName());
        gen.writeNumberField("value", measurement.getValue());
        gen.writeEndObject();
    }
}