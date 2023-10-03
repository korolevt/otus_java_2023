package org.kt.service;

import org.kt.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/***
 * Взаимодействие между двумя потоками реализовано с помощью очереди BlockingQueue.
 * Поток записи микрофона вставляет звук с микрофона в очередь с помощью блокирующего метода put,
 * и ждет пока другой поток не заберет данные.
 * Поток передачи аудио проверяет очередь с помощью метода poll
 * и когда в очереди появляются данные, то забирает их для отправки и удаляет из очереди
 * (удаление из очереди, приводит к разблокировке потока записи микрофона и цикл повторяется)
 * Здесь аудио отправляется равными кусочками размером CHUCK_SIZE,
 * но время между отправками может незначительно различаться.
 */


public class MicrophoneServiceQueue implements MicrophoneService {
    private final Logger logger = LoggerFactory.getLogger(MicrophoneServiceQueue.class);

    private final Handler handler;
    private final int sampleRate;
    private final int durationMs;

    private boolean running;
    private final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<>();

    private TargetDataLine targetDataLine;

    @Override
    public int getSampleRate() { return this.sampleRate; }

    public MicrophoneServiceQueue(Handler handler, int sampleRate, int durationMs) {
        this.handler = handler;
        this.sampleRate = sampleRate;
        this.durationMs = durationMs;

        running = true;

        Thread thMicrophone = new Thread(new MicrophoneThread());
        thMicrophone.start();

        Thread thSend = new Thread(new SendThread());
        thSend.start();
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

                while (running && targetDataLine != null) {
                    byte[] data = new byte[CHUNK_SIZE];
                    int numBytesRead = targetDataLine.read(data, 0, data.length);
                    logger.info("Microphone read: " + numBytesRead);
                    if (numBytesRead != -1 && running) {
                        queue.put(data);
                    }
                }

                logger.info("Microphone thread end");
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
                while(running) {
                    byte[] data = queue.poll(10, TimeUnit.MICROSECONDS);
                    if (data != null) {
                        handler.sendAudio(data, targetDataLine.getFormat());
                        logger.info("send data: " + data.length);
                    }
                }
                logger.info("Send thread end");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
