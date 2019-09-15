package com.yang.tutorial.service;

import com.yang.tutorial.model.Scope;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangz
 */
@Slf4j
public class PersistentScopeService {

    private Scope oldScope;

    public PersistentScopeService(Scope oldScope) {
        this.oldScope = oldScope;
    }

    public Scope queryScope() throws InterruptedException {
        Thread.sleep(500);
        return oldScope;
    }

    public void delete(String subId) throws InterruptedException {
        Thread.sleep(50);
        log.info("delete: {}", subId);
    }

    public void update(String subId) throws InterruptedException {
        Thread.sleep(50);
        log.info("update: {}", subId);
    }

}
