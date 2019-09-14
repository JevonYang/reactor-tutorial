package com.yang.tutorial.scheduler;

import com.yang.tutorial.pool.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.*;

@Slf4j
public class FlatMapModel {

    @Test
    public void apply() throws InterruptedException {

        ExecutorService executorService = ThreadPoolService.getInstance("flatMap-pool-%d");
        CountDownLatch finishedLatch = new CountDownLatch(1);

        Flux.range(0, 200).subscribeOn(Schedulers.newElastic("sub")).flatMap(i -> {
            return Mono.just(i).subscribeOn(Schedulers.fromExecutor(executorService))
                    .map(integer -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            log.error("InterruptedException: ", ex);
                        }
                        log.info("中间过程： {}", i);
                        return integer + "";
                    });
        }).publishOn(Schedulers.immediate()).subscribe(result -> {
            log.info("======================> 输出内容： {}", result);
        }, throwable -> {

        }, () -> {
            finishedLatch.countDown();
        });

        finishedLatch.await();
        executorService.shutdown();
    }


}
