package com.yang.tutorial.callback;

/**
 * @author yangzijing
 */
public class Worker {

    void work(Callback<String> callback, String someWork) {
        for (int i = 0; i < 100; i++) {
            System.out.println("is working...");
        }
        callback.apply(someWork + " is done!");
    }

}
