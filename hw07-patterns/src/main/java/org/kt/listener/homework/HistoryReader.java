package org.kt.listener.homework;

import org.kt.model.Message;

import java.util.Optional;

public interface HistoryReader {

    Optional<Message> findMessageById(long id);
}
