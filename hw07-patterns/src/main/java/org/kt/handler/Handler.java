package org.kt.handler;

import org.kt.listener.Listener;
import org.kt.model.Message;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
