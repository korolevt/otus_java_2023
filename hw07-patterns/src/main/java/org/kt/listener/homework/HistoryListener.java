package org.kt.listener.homework;

import org.kt.listener.Listener;
import org.kt.model.Message;

import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    @Override
    public void onUpdated(Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        throw new UnsupportedOperationException();
    }
}
