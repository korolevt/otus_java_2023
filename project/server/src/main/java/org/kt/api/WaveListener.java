package org.kt.api;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.sound.sampled.AudioFormat;

/***
 * Слушатель, который добавляем Wave заголовок к кусочкам звука, делая
 * для передачи Wav-файл
 */
public class WaveListener implements Listener {

    private final WebSocketSession session;

    public WaveListener(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void onNextAudio(byte[] audio, AudioFormat audioFormat) {
        try {
            if (audio.length > 0) {
                // Добавляем wave header
                byte[] header = WaveHeader.build(audioFormat.getChannels(), (int)audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits(), audio.length / audioFormat.getFrameSize());
                audio = ArrayUtils.addAll(header, audio);
                session.sendMessage(new BinaryMessage(audio));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}