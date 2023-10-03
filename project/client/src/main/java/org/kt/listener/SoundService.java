package org.kt.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;

@Service
public class SoundService {
    private static final Logger logger = LoggerFactory.getLogger(SoundService.class);

    private SourceDataLine line;

    public void toSpeaker(byte[] sound, int sampleRate) {
        try {
            AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
            if (line == null) {
                line = getSourceDataLine(format);
            }
            if (line != null) {
                line.open(format);
                line.start();
                line.write(sound, 0, sound.length);
            }
        } catch (Exception e) {
            logger.error("Not working in speakers...", e);
        }
    }

    public void close() {
        if (line != null)
            line.close();
        line = null;
    }

    static private SourceDataLine getSourceDataLine(AudioFormat format) {
        return (SourceDataLine)getDefaultSourceLine(format, SourceDataLine.class);
    }

    static private Line getDefaultSourceLine(AudioFormat format, Class<?> type) {
        DataLine.Info dataLineInfo = new DataLine.Info(type, format);
        Line line = null;
        try {
            if (!AudioSystem.isLineSupported(dataLineInfo)) {
                logger.error("Line is not supported");
                return null;
            }
            line = AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e) {
            logger.error("Line Unavailable!");
        }
        return line;
    }
}
