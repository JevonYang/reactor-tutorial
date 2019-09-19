package com.yang.tutorial.imperative;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author yangzijing
 */
@Slf4j
public class ImperBoss {

    private ImperWorker worker;

    public ImperBoss(ImperWorker worker) {
        this.worker = worker;
    }

    public void makeBigDeals(final String someDetail) throws ExecutionException, InterruptedException {
        log.info("分配工作...");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> worker.work(someDetail));
        log.info("分配完工作。");
        log.info("老板下班回家了。。。。");
        log.info("boss got the feedback from worker: {}", future.get());
    }

}
