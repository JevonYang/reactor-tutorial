package com.yang.tutorial.scheduler;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @author yangz
 */
@Slf4j
public class SubPubModel {

    private final static int Count = 100;

    @Test
    public void apply() throws InterruptedException {
        ExecutorService es = ThreadPoolService.getInstance("pub-pool-%d");
        CountDownLatch finishedLatch = new CountDownLatch(1);

        long t = System.nanoTime();
        Flux.range(0, Count)
                .publishOn(Schedulers.fromExecutorService(es))
                .map(i -> {
                    log.info("中间过程 {}", i);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex) {
                        log.error("InterruptedException: ", ex);
                    }
                    return i + "";
                })
                .subscribeOn(Schedulers.newElastic("elastic-pool"))
                .subscribe(result -> {
                    log.info("======> 结果 {}", result);
                }, throwable -> {

                }, () -> {
                    finishedLatch.countDown();
                });
        finishedLatch.await();
        t = (System.nanoTime() - t) / 1_000_000; //ms
        System.out.println("SubPubModel TPS: " + Count * 1000 / t);
        es.shutdown();
    }

}
