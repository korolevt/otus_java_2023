package org.kt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.IntStream;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private boolean last = true;

    private synchronized void action(boolean flag) {
        var sequence = IntStream.concat(
            IntStream.range(1, 10),
            IntStream.iterate(10, i -> i > 0, i -> i - 1)).iterator();

        while(!Thread.currentThread().isInterrupted() && sequence.hasNext()) {
            try {
                while (last == flag) {
                    this.wait();
                }

                logger.info("{}", sequence.next());
                last = flag;

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        new Thread(() -> main.action(false)).start();
        new Thread(() -> main.action(true)).start();
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
