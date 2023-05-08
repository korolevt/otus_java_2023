package org.kt.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.kt.model.Measurement;
import org.kt.model.MeasurementSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        mapper = createObjectMapper();
        this.fileName = fileName;
    }

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule()
                .addSerializer(new MeasurementSerializer(Measurement.class)));
        return mapper;
    }
    @Override
    public void serialize(Map<String, Double> data) {
        var file = new File(fileName);

        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
