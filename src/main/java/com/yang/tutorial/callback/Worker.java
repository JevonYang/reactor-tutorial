package com.yang.tutorial.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class Worker {

    public void work(Callback<String> callback, String someWork) {
        String result = "(" + someWork + ")";
        callback.callback(result);
    }

}
