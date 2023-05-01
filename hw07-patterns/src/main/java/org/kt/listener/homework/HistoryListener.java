package org.kt.listener.homework;

import org.kt.listener.Listener;
import org.kt.model.Message;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Deque<Message> stack = new ArrayDeque<>();

    @Override
    public void onUpdated(Message msg) {
        stack.push(msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return stack.stream().filter(msg -> msg.getId() == id).findFirst();
    }
}
