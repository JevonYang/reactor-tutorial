package com.yang.tutorial.scheduler;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

@Slf4j
public class ParallelModel {

    private final static int Count = 100;
    private final static int PARARLLEL = 100;

    @Test
    public void apply() throws InterruptedException {

        ExecutorService es = ThreadPoolService.getInstance("parallel-pool-%d");

        CountDownLatch finishedLatch = new CountDownLatch(1);

        long t = System.nanoTime();
        Flux.range(0, Count)
                .parallel(PARARLLEL)
                .map(integer -> {
                    log.info("before RunOn {}", integer);
                    return integer;
                })
                .runOn(Schedulers.fromExecutor(es))
                .map(i -> {
                    try {
                        log.info("{} is running", i);
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        log.error("InterruptedException: ", ex);
                    }
//                    log.info("中间过程 {}", i);
                    return i + "";
                })
                .subscribe(result -> {
                    log.info("======> 结果 {}", result);
                }, throwable -> {

                }, () -> {
                    finishedLatch.countDown();
                });

        finishedLatch.await();
        t = (System.nanoTime() - t) / 1000000; //ms
        System.out.println("ParallelModel TPS: " + Count * 1000 / t);
        es.shutdown();
    }
}
