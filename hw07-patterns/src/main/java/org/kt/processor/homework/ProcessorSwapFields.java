package org.kt.processor.homework;

import org.kt.model.Message;
import org.kt.processor.Processor;

public class ProcessorSwapFields implements Processor {
    @Override
    public Message process(Message message) {
        var field11 = message.getField11();
        var field12 = message.getField12();
        return message.toBuilder().field11(field12).field12(field11).build();
    }
}
