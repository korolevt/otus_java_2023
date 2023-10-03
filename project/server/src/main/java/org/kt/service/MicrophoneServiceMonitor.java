package org.kt.service;

import org.kt.handler.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

/***
 * Синхронизация потока записи микрофона и потока передачи аудио с помощью
 * монитора и операций notify и wait.
 * Здесь аудио отправляется равными кусочками размером CHUCK_SIZE,
 * но время между отправками может незначительно различаться.
 */


public class MicrophoneServiceMonitor implements MicrophoneService {
    private final Logger logger = LoggerFactory.getLogger(MicrophoneServiceMonitor.class);

    private final Handler handler;
    private final int sampleRate;
    private final int durationMs;

    private boolean running;
    private final Object monitor = new Object();
    private boolean dataSet = false;
    private byte[] data;

    private TargetDataLine targetDataLine;

    @Override
    public int getSampleRate() { return this.sampleRate; }

    public MicrophoneServiceMonitor(Handler handler, int sampleRate, int durationMs) {
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

                data = new byte[CHUNK_SIZE];

                logger.info("Microphone thread start");

                while (running && targetDataLine != null) {
                    synchronized (monitor) {
                        while (dataSet && running) {
                            monitor.wait();
                        }
                        int numBytesRead = targetDataLine.read(data, 0, data.length);
                        logger.info("Microphone read: " + numBytesRead);
                        if (numBytesRead != -1 && running) {
                            dataSet = true;
                            monitor.notify();
                        }
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
                    byte[] dataCopy;
                    synchronized(monitor) {
                        while (!dataSet && running) {
                            monitor.wait();
                        }
                        dataCopy = data.clone();
                        dataSet = false;
                        monitor.notify();
                    }
                    if (dataCopy != null) {
                        handler.sendAudio(dataCopy, targetDataLine.getFormat());
                        logger.info("send data: " + dataCopy.length);
                    }
                }

                logger.info("Send thread end");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }


        }
    }
}
