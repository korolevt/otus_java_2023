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

    private final ObjectMapper mapper = new ObjectMapper();

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var file = new File(fileName);

        mapper.registerModule(new SimpleModule()
                .addSerializer(new MeasurementSerializer(Measurement.class)));

        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
