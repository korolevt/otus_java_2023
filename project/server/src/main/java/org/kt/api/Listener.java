package org.kt.api;

import javax.sound.sampled.AudioFormat;

public interface Listener {
    void onNextAudio(byte[] audio, AudioFormat audioFormat);
}
