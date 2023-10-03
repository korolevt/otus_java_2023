package org.kt.handler;

import org.kt.api.Listener;
import javax.sound.sampled.AudioFormat;

public interface Handler {
    void addListener(Listener listener);
    void removeListener(Listener listener);

    void sendAudio(byte[] audio, AudioFormat audioFormat);
}
