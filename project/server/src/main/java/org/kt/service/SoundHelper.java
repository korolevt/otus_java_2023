package org.kt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;

public class SoundHelper {
    private static final Logger logger = LoggerFactory.getLogger(SoundHelper.class);

    public static TargetDataLine getTargetDataLine(AudioFormat format) {
        return (TargetDataLine) getDefaultSourceLine(format, TargetDataLine.class);
    }

    public static Clip getClip(AudioFormat format) {
        return (Clip) getDefaultSourceLine(format, Clip.class);
    }

    private static Line getDefaultSourceLine(AudioFormat format, Class<?> type) {
        DataLine.Info dataLineInfo = new DataLine.Info(type, format);
        Line line = null;
        try {
            if (!AudioSystem.isLineSupported(dataLineInfo)) {
                System.err.println("Line is not supported");
                return null;
            }
            line = AudioSystem.getLine(dataLineInfo);
        } catch (LineUnavailableException e) {
            System.out.println("Line Unavailable!");
        }
        return line;
    }

    public static TargetDataLine initMicrophone(int sampleRate) {
        TargetDataLine targetDataLine = null;
        try {
            AudioFormat audioFormat = new AudioFormat(sampleRate,16,1,true,false);
            // Включаем запись на микрофоне
            targetDataLine = getTargetDataLine(audioFormat);
            if (targetDataLine != null) {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
            }
        } catch (LineUnavailableException e) {
            logger.error(e.getMessage(), e);
        }
        return targetDataLine;
    }
}
