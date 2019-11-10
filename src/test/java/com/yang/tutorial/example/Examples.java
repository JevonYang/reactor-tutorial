package com.yang.tutorial.example;

import com.yang.tutorial.callback.Boss;
import com.yang.tutorial.callback.Callback;
import com.yang.tutorial.callback.Worker;
import com.yang.tutorial.imperative.ImperBoss;
import com.yang.tutorial.imperative.ImperWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;
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
    public void callbackHell() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 主线程是boss
        new Callback<String>() {
            private Worker productManager = new Worker();
            @Override
            public void callback(String s) {
                log.info("产品经理：开始工作");
                String midResult = "设计(" + s + ")";
                log.info("产品经理：处理任务并给出原型: " + midResult);
                log.info("产品经理：将任务交给程序员");
                new Thread(() -> {
                    new Callback<String>() {
                        private Worker coder = new Worker();

                        @Override
                        public void callback(String s) {
                            String result = "编程(" + s + ")";
                            log.info("程序员：完成任务{}", result);
                            countDownLatch.countDown();
                        }

                        public void coding(String coding) {
                            log.info("程序员：开始工作");
                            coder.work(this, coding);
                        }

                    }.coding(midResult);
                }, "coder").start();
            }

            public void makeBigDeals(String bigDeal) {
                log.info("老板：将任务交给产品");
                new Thread(() -> {
                    this.productManager.work(this, bigDeal);
                }, "Product").start();
            }
        }.makeBigDeals("项目");
        log.info("老板：下班回家");
        countDownLatch.await();
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
