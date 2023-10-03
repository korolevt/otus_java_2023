package org.kt.config;

import org.kt.api.WebSocketHandler;
import org.kt.handler.Handler;
import org.kt.handler.HandlerImpl;
import org.kt.service.MicrophoneService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MicrophoneService microphoneService;
    private final Handler handler;

    public WebSocketConfig(MicrophoneService microphoneService, HandlerImpl handler) {
        this.microphoneService = microphoneService;
        this.handler = handler;
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxBinaryMessageBufferSize(102400);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(microphoneService, handler), "/api").setAllowedOrigins("*");
    }
}

