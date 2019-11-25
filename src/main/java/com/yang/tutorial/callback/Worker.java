package com.yang.tutorial.callback;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class Worker {

    public void work(Callback<String> callback, String someWork) {
        String result = "(" + someWork + ")";
        log.info("完成工作： {}", result);
        callback.callback(result);
    }

}
