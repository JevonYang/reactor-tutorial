package com.yang.tutorial.scheduler;

import com.yang.tutorial.pool.ThreadPoolService;
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

    @Test
    public void apply() throws InterruptedException {
        ExecutorService es = ThreadPoolService.getInstance("flatMap-pool-%d");
        CountDownLatch finishedLatch = new CountDownLatch(1);

        Flux.range(0, 100)
                .publishOn(Schedulers.fromExecutor(es))
                .map(i -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        log.error("InterruptedException: ", ex);
                    }
                    log.info("中间过程 {}", i);
                    return i + "";
                })
                .publishOn(Schedulers.fromExecutor(es))
                .subscribe(result -> {
                    log.info("======> 结果 {}", result);
                }, throwable -> {

                }, () -> {
                    finishedLatch.countDown();
                });

        finishedLatch.await();
        es.shutdown();
    }

}
