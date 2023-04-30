package org.kt.processor.homework;

import org.kt.model.Message;
import org.kt.processor.Processor;

public class ProcessorTime implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorTime(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        System.out.println(dateTimeProvider.getDate());
        if (dateTimeProvider.getDate().getSecond() % 2 == 0)
            throw new TimeException("Exception on an even second");
        return message;
    }

    public static class TimeException extends RuntimeException {
        public TimeException(String message) {
            super(message);
        }
    }
}
