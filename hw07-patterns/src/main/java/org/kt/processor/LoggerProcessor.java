package org.kt.processor;

import org.kt.model.Message;

public class LoggerProcessor implements Processor {


    private final Processor processor;

    public LoggerProcessor(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        System.out.println("log processing message:" + message);
        return processor.process(message);
    }
}
