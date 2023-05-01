package org.kt.processor;

import org.kt.model.Message;

public interface Processor {

    Message process(Message message);
}
