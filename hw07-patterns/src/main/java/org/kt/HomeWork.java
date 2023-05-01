package org.kt;

import org.kt.handler.ComplexProcessor;
import org.kt.listener.ListenerPrinterConsole;
import org.kt.model.Message;
import org.kt.processor.LoggerProcessor;
import org.kt.processor.ProcessorConcatFields;
import org.kt.processor.homework.ProcessorSwapFields;
import org.kt.processor.ProcessorUpperField10;
import org.kt.processor.homework.ProcessorTime;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {
    public static void main(String[] args) {
        var processors = List.of(
                new ProcessorConcatFields(),
                new ProcessorSwapFields(),
                new LoggerProcessor(new ProcessorUpperField10()),
                new ProcessorTime(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field11("field11")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
