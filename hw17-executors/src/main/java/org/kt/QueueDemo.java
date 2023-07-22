package org.kt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Взаимодействие между двумя потоками реализовано с помощью очереди BlockingQueue.
 * 1-й поток передает последовательность чисел через очередь во 2-й поток
 * 1-й поток (producer) вставляет число в очередь с помощью блокирующего метода put, и ждет
 * пока другой поток заберет число.
 * 2-й поток (consumer) проверяет наличие числа в очереди с помощью метода poll
 * и когда появляется число в очереди, то удаляет его из очереди и выводит в лог
 * (удаление из очереди, приводит к разблокировке потока 1 и цикл повторяется)
 * После завершения потока 1, сбрасывается флаг processFlag, что приводит к остановке потока 2.
 */

public class QueueDemo {
    private static final Logger logger = LoggerFactory.getLogger(QueueDemo.class);

    private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
    private AtomicBoolean processFlag;

    private void producer() {
        var sequence = IntStream.concat(
                IntStream.range(1, 10),
                IntStream.iterate(10, i -> i > 0, i -> i - 1)).iterator();

        while(!Thread.currentThread().isInterrupted() && sequence.hasNext()) {
            try {
                int number = sequence.next();
                logger.info("{}", number);

                queue.put(number);
                sleep();
            }
            catch (InterruptedException ex) {
               Thread.currentThread().interrupt();
            }
        }
        processFlag.set(false);
    }

    private void consumer() {
        while(processFlag.get()) {
            var number = queue.poll();
            if (number != null) {
                logger.info("{}", number);
            }
            sleep();
        }
    }

    public static void main(String[] args) {
        QueueDemo demo = new QueueDemo();
        demo.start();
    }

    private void start() {
        processFlag = new AtomicBoolean(true);
        new Thread(this::producer).start();
        new Thread(this::consumer).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
