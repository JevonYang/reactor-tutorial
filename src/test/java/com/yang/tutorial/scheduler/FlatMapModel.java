package com.yang.tutorial.scheduler;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.*;

@Slf4j
public class FlatMapModel {

    private final static int Count = 100;

    ExecutorService executorService = ThreadPoolService.getInstance("flatMap-pool-%d");

    @Test
    public void apply() throws InterruptedException {

        CountDownLatch finishedLatch = new CountDownLatch(1);
        long t = System.nanoTime();
        Flux.range(0, Count).flatMap(i -> {
            log.info("before {}", i);
            return Mono.just(i).subscribeOn(Schedulers.fromExecutor(executorService))
                    .map(integer -> {
                        log.info("is running： {}", i);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            log.error("InterruptedException: ", ex);
                        }
                        return integer + "";
                    });
        }).publishOn(Schedulers.immediate()).subscribe(result -> {
            log.info("============> 输出内容： {}", result);
        }, throwable -> {

        }, () -> {
            finishedLatch.countDown();
        });

        finishedLatch.await();
        t = (System.nanoTime() - t) / 1000000; //ms
        System.out.println("FlatMapModel TPS: " + Count * 1000 / t);
        executorService.shutdown();
    }


}
