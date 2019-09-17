package com.yang.tutorial.observable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yangzijing
 */
public class Speaker implements Observable {

    private List<Observer> observers;

    public Speaker() {
        List<Observer> list = new LinkedList<>();
        this.observers = list;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(Object args) {
        observers.forEach(ob -> {
            ob.update(args);
        });

    }
}
