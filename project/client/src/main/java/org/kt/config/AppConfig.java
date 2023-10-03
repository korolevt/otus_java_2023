package org.kt.config;

import org.kt.listener.SoundService;
import org.kt.listener.WebSocketSoundListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public WebSocketSoundListener webSocketService (
            SoundService soundService,
            @Value("${sound.url}") String url) {

        return new WebSocketSoundListener(soundService, url);
    }
}
