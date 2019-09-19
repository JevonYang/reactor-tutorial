package com.yang.tutorial.example;

import com.yang.tutorial.callback.Boss;
import com.yang.tutorial.callback.Worker;
import com.yang.tutorial.imperative.ImperBoss;
import com.yang.tutorial.imperative.ImperWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

@Slf4j
public class Examples {

    @Test
    public void imperative() throws ExecutionException, InterruptedException {
        ImperWorker worker = new ImperWorker();
        ImperBoss boss = new ImperBoss(worker);
        boss.makeBigDeals("coding");
    }

    @Test
    public void callback() {
        Worker worker = new Worker();
        Boss boss = new Boss(worker);
        boss.makeBigDeals("coding");
    }

    @Test
    public void reactive() {
        Mono.just("coding")
                .map(work -> {
                    for (int i = 0; i < 100; i++) {
                        log.info("worker is working");
                    }
                    return work + " is done!";
                }).subscribe(result -> {
                    log.info("boss got the feedback from worker: {}", result);
                });

    }

}
