package com.yang.tutorial.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class Worker {

    void work(Callback<String> callback, String someWork) {
        for (int i = 0; i < 100; i++) {
            log.info("worker is working...");
        }
        callback.apply(someWork + " is done!");
    }

}
