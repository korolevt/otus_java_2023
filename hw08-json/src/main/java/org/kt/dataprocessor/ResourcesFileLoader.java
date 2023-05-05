package org.kt.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.kt.model.Measurement;
import org.kt.model.MeasurementDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    private final ObjectMapper mapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        var stream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);

        mapper.registerModule(new SimpleModule()
                .addDeserializer(Measurement.class, new MeasurementDeserializer()));

        try {
            var measurements = mapper.readValue(stream, Measurement[].class);
            return Arrays.asList(measurements);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
