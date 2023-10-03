package org.kt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.websocket.ClientWebSocketContainer;
import org.springframework.integration.websocket.WebSocketListener;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.List;

public class WebSocketSoundListener implements WebSocketListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSoundListener.class);

    private final String url;
    private final SoundService soundService;

    private WebSocketSession session;
    private int sampleRate;

    public WebSocketSoundListener(SoundService soundService, String url) {
        this.soundService = soundService;
        this.url = url;
    }

    public void connect() {
        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        ClientWebSocketContainer container = new ClientWebSocketContainer(simpleWebSocketClient, url);
        container.start();
        container.setMessageListener(this);
        session = container.getSession(null);
        logger.info("Connect successful");
    }

    private byte[] readWaveData(byte[] allBytes) {
        byte[] data = new byte[allBytes.length - 44];
        System.arraycopy(allBytes, 44, data, 0, data.length);
        return data;
    }

    @Override
    public void onMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        if (webSocketMessage instanceof BinaryMessage) {
            if (sampleRate > 0) {
                BinaryMessage message = (BinaryMessage) webSocketMessage;
                byte[] audio = message.getPayload().array();
                audio = readWaveData(audio);

                soundService.toSpeaker(audio, sampleRate);
            }
        }
        if (webSocketMessage instanceof TextMessage) {
            TextMessage message = (TextMessage) webSocketMessage;
            sampleRate = Integer.parseInt(message.getPayload());
            logger.info("SampleRate: " + sampleRate);
        }
    }

    @Override
    public void afterSessionStarted(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("GET_SAMPLE_RATE"));
        logger.info("Send message GET_SAMPLE_RATE");
    }

    @Override
    public void afterSessionEnded(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        session.close();
    }

    @Override
    public List<String> getSubProtocols() {
        return null;
    }
}
