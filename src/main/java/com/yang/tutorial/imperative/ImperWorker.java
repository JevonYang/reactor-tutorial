package com.yang.tutorial.imperative;

import com.yang.tutorial.callback.Callback;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class ImperWorker {

    public String work(String someWork) throws InterruptedException {
        log.info("员工开始工作");
        Thread.sleep(2000);
        return someWork + " is done!";
    }

}
