package org.kt.api;

import org.kt.api.model.SensorData;

public interface SensorsDataServer {
    void onReceive(SensorData sensorData);
}
