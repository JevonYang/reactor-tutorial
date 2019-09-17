package com.yang.tutorial.observable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author yangzijing
 */
public class Flow<T> implements Observable {

    private T t;

    private List<Observer> observers;

    public Flow() {
        this.observers = new LinkedList<Observer>();
    }

    public Flow<T> create(T t) {
        this.t = t;
        return this;
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
        observer.update(t);
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
        this.observers.forEach(observer -> {
            observer.update(args);
        });
    }
}
