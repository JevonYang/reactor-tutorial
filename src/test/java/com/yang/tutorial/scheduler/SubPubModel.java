package com.yang.tutorial.scheduler;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yangz
 */
@Slf4j
public class SubPubModel {

    private final static int Count = 100;

    ExecutorService es = ThreadPoolService.getInstance("pub-pool-%d");

    @Test
    public void apply() throws InterruptedException {

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

    @Test
    public void threadModel() throws InterruptedException {

        CountDownLatch finishedLatch = new CountDownLatch(1);
        Flux.range(1, 5)
                .map(i-> {
                    log.info("zero: {} runs", i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i;
                })
                .subscribeOn(Schedulers.fromExecutor(Executors.newFixedThreadPool(5)))
                .publishOn(Schedulers.parallel())
                .map(i-> {
                    log.info("first: {} runs", i);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return i+1;
                })
                .publishOn(Schedulers.fromExecutorService(es))
                .subscribeOn(Schedulers.fromExecutor(Executors.newCachedThreadPool()))
                .map(i-> {
                    log.info("second: {} runs", i);
                    return i+1;
                })
                .publishOn(Schedulers.newParallel("hello"))
                .subscribeOn(Schedulers.newElastic("world"))
                .subscribe(i-> {
                    log.info("             subscribed {}", i);
                }, throwable -> {

                }, finishedLatch::countDown);

        finishedLatch.await();
    }

}
