package org.kt.handler;

import org.kt.api.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class HandlerImpl implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(HandlerImpl.class);

    private final Queue<Listener> listeners = new ConcurrentLinkedQueue<>();

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
        logger.info("Add listener " +listener.toString());
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
        logger.info("Remove listener " +listener.toString());
    }

    @Override
    public void sendAudio(byte[] audio, AudioFormat audioFormat) {
        listeners.forEach(listener -> {
            try {
                listener.onNextAudio(audio, audioFormat);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        });
    }
}
