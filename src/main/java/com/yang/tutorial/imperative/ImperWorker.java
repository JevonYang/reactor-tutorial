package com.yang.tutorial.imperative;

import com.yang.tutorial.callback.Callback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class ImperWorker {

    public String work(String someWork) {
        for (int i = 0; i < 100; i++) {
            log.info("worker is working...");
        }
        return someWork + " is done!";
    }

}
