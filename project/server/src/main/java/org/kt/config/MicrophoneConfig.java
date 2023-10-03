package org.kt.config;

import org.kt.handler.Handler;
import org.kt.handler.HandlerImpl;
import org.kt.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MicrophoneConfig {

    private final Handler handler;

    public MicrophoneConfig(HandlerImpl handler) {
        this.handler = handler;
    }

    @Bean
    public MicrophoneService microphoneService(
        @Value("${microphone.sample_rate}") int sampleRate,
        @Value("${microphone.duration_ms}") int durationMs) {

        return new MicrophoneServiceExecutors(handler, sampleRate, durationMs);
//        return new MicrophoneServicePiped(handler, sampleRate, durationMs);
//        return new MicrophoneServiceMonitor(handler, sampleRate, durationMs);
//        return new MicrophoneServiceQueue(handler, sampleRate, durationMs);
    }
}
