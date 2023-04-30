package org.kt.listener;

import org.kt.model.Message;

public interface Listener {

    void onUpdated(Message msg);
}
