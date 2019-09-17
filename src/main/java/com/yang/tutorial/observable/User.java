package com.yang.tutorial.observable;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangzijing
 */
@Slf4j
public class User implements Observer {

    private String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public void update(Object arg) {
        log.info("{} got messages: {}, and do something.", name, arg);
    }
}
