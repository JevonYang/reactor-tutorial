package com.yang.tutorial.observable;

/**
 * @author yangzijing
 */
public interface Observable {

    //    public void subscribe(Observer observer);


    /**注册观察者
     *
     * @param observer
     * @return
     */
    public void registerObserver(Observer observer);


    /**
     * 移除观察者
     *
     * @param observer
     */
    public void removeObserver(Observer observer);


    /**
     * 通知观察者
     *
     * @param args 通知内容
     */
    public void notifyObserver(Object args);

}
