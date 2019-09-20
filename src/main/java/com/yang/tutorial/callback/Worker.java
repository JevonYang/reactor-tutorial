package com.yang.tutorial.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class Worker {

    public void work(Callback<String> callback, String someWork) {
//        for (int i = 0; i < 100; i++) {
//            log.info("worker is working...");
//        }
//        Thread.sleep(2000);
        callback.callback(someWork + " is done!");
    }

}
