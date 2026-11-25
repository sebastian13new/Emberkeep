package com.emberkeep.emberkeep.ui;

import java.util.ArrayList;
import java.util.List;

/** Penghubung antara yang mengirim event dan yang mendengarkan. */
public class EventBus {
    private final List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void publish(GameEvent event) {
        for (Observer o : observers) {
            o.onEvent(event);
        }
    }
}
