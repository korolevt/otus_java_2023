package org.kt.service;

import org.kt.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * Поток записи микрофона создает исполнителя Executors.newSingleThreadExecutor(),
 * который выполняет одну задачу передачи аудио подписчикам за раз.
 * Сначала вычисляется буфер CHUNK_SIZE для получения данных с микрофона за N mc.
 * Далее при заполнении буфера создается задача для отправки аудио подписчикам и
 * продолжается работа по захвату аудио с микрофона.
 * Таким образом аудио отправляется равными кусочками размером CHUCK_SIZE,
 * но время между отправками может незначительно различаться.
 */


public class MicrophoneServiceExecutors implements MicrophoneService {
    private final Logger logger = LoggerFactory.getLogger(MicrophoneServiceExecutors.class);

    private final Handler handler;
    private final int sampleRate;
    private final int durationMs;

    private boolean running;
    private TargetDataLine targetDataLine;

    @Override
    public int getSampleRate() { return this.sampleRate; }

    public MicrophoneServiceExecutors(Handler handler, int sampleRate, int durationMs) {
        this.handler = handler;
        this.sampleRate = sampleRate;
        this.durationMs = durationMs;

        running = true;

        Thread thMicrophone = new Thread(new MicrophoneThread());
        thMicrophone.start();
    }


    /**
     * Поток для записи аудио с микрофона
     */
    private class MicrophoneThread implements Runnable  {

        @Override
        public void run() {
            try {
                targetDataLine = SoundHelper.initMicrophone(sampleRate);
                AudioFormat audioFormat = targetDataLine.getFormat();
                logger.info(String.format("Microphone start (sampleRate=%d, duration_ms=%d)", sampleRate, durationMs));

                final int CHUNK_SIZE = (int)(audioFormat.getSampleRate()) *
                        audioFormat.getChannels() *
                        (audioFormat.getSampleSizeInBits() / 8) *
                        durationMs / 1000;

                logger.info("Microphone thread start");

                ExecutorService executorService = Executors.newSingleThreadExecutor();

                while (running && targetDataLine != null) {
                    byte[] data = new byte[CHUNK_SIZE];
                    int numBytesRead = targetDataLine.read(data, 0, data.length);
                    //logger.info("Microphone read: " + numBytesRead);
                    if (numBytesRead != -1 && running) {
                        executorService.execute(new SendTask(data));
                    }
                }
                logger.info("Microphone thread end");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Задача для передачи аудио подписчикам.
     */
     private class SendTask implements Runnable  {
        private final byte[] data;

        public SendTask(byte[] audio) {
            data = audio.clone();
        }

        @Override
        public void run() {
            if (data != null) {
                handler.sendAudio(data, targetDataLine.getFormat());
                //logger.info("send data: " + data.length);
            }
        }
    }
}
