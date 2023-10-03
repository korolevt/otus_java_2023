package org.kt.service;

import org.kt.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

/***
 * Синхронизация потока записи микрофона и потока передачи аудио с помощью
 * конвейерных потоков PipedOutputStream и PipedInputStream.
 * PipedInputStream - это поточный входной поток, который подключается к
 * поточному выходному потоку PipedOutputStream,
 * Затем в PipedOutputStream записываются данные c микрофона, после чего они могут
 * быть считаны в PipedInputStream и переданы подписчикам.
 * Здесь аудио может отправляться кусочками разных размеров, но строго каждые N mc.
 */


public class MicrophoneServicePiped implements MicrophoneService {
    private final Logger logger = LoggerFactory.getLogger(MicrophoneServicePiped.class);

    private final Handler handler;
    private final int sampleRate;
    private final int durationMs;

    private final int SIZE_BUFFER = 8000;

    private boolean running;
    private TargetDataLine targetDataLine;

    private final PipedInputStream in;
    private final PipedOutputStream out;


    @Override
    public int getSampleRate() { return this.sampleRate; }

    public MicrophoneServicePiped(Handler handler, int sampleRate, int durationMs) {
        try {
            this.handler = handler;
            this.sampleRate = sampleRate;
            this.durationMs = durationMs;

            running = true;

            in = new PipedInputStream(60000);
            out = new PipedOutputStream(in);

            Thread thMicrophone = new Thread(new MicrophoneThread());
            thMicrophone.start();

            Thread thSend = new Thread(new SendThread());
            thSend.start();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * Поток для записи аудио с микрофона
     */
    private class MicrophoneThread implements Runnable  {

        @Override
        public void run() {
            try {
                targetDataLine = SoundHelper.initMicrophone(sampleRate);
                //AudioFormat audioFormat = targetDataLine.getFormat();
                logger.info(String.format("Microphone start (sampleRate=%d, duration_ms=%d)", sampleRate, durationMs));

                final byte[] data = new byte[100];

                logger.info("Microphone thread start");

                while (running && targetDataLine != null) {
                    int numBytesRead = targetDataLine.read(data, 0, data.length);
                    logger.info("Microphone read: " + numBytesRead);
                    if (numBytesRead != -1 && running) {
                        out.write(data, 0, numBytesRead);
                    }
                    Thread.sleep(0);
                }
                out.close();
                logger.info("microphone thread end");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Поток для передачи аудио подписчикам.
     */
     private class SendThread implements Runnable  {

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[SIZE_BUFFER];
                while (running) {
                    while (in.available() > 0) {
                        int count = in.read(buffer, 0, buffer.length);
                        byte[] data = Arrays.copyOfRange(buffer, 0, count);
                        handler.sendAudio(data, targetDataLine.getFormat());
                        logger.info("send data: " + data.length);
                    }
                    Thread.sleep(durationMs);  // Передаем пакеты раз в N мс
                }
                in.close();
                logger.info("Send thread end");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
