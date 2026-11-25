package com.emberkeep.emberkeep.ui;

/** Observer pattern: dipanggil setiap ada event dari EventBus. */
public interface Observer {
    void onEvent(GameEvent event);
}
