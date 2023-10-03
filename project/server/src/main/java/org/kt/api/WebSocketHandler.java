package org.kt.api;

import org.kt.handler.Handler;
import org.kt.service.MicrophoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import java.util.HashMap;
import java.util.Map;

public class WebSocketHandler extends AbstractWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private final Map<String, Listener> sessions = new HashMap<>();
    private final MicrophoneService microphoneService;
    private final Handler handler;


    public WebSocketHandler(MicrophoneService microphoneService, Handler handler) {
        this.microphoneService = microphoneService;
        this.handler = handler;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Listener listener = new WaveListener(session);
        sessions.put(session.getId(), listener);
        logger.info("Connect session " + session.getId());

        handler.addListener(listener);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Listener listener = sessions.get(session.getId());
        sessions.remove(session);
        logger.info("Disconnect session " + session.getId());

        handler.removeListener(listener);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayload().equalsIgnoreCase("GET_SAMPLE_RATE")) {
            session.sendMessage(new TextMessage(microphoneService.getSampleRate() + ""));
            logger.info("GET_SAMPLE_RATE, return {}", microphoneService.getSampleRate());
        }
    }
}
