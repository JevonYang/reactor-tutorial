package com.yang.tutorial.example;

import com.yang.tutorial.callback.*;
import com.yang.tutorial.imperative.ImperBoss;
import com.yang.tutorial.imperative.ImperWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
    public void callback() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Callback<String>() {
            Worker worker = new Worker();
            @Override
            public void callback(String s) {
                log.info("老板拿到结果： {}", s);
                countDownLatch.countDown();
            }

            void makeBigDeal(String deal) {
                log.info("分配工作...");
                new Thread(() -> worker.work(this, deal), "worker").start();
                log.info("分配完工作。");
                log.info("老板下班回家了。。。。");
            }
        }.makeBigDeal("A big deal");
        countDownLatch.await();
}

    @Test
    public void callbackHell() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 主线程是boss
        new Callback<String>() {
            private Worker productManager = new Worker();
            @Override
            public void callback(String s) {
                log.info("老板：拿到结果，交给程序员 {}", s);
                new Thread(() -> {
                    new Callback<String>() {
                        private Worker coder = new Worker();

                        @Override
                        public void callback(String s) {
                            log.info("程序员：完成任务{}", s);
                            countDownLatch.countDown();
                        }

                        public void coding(String coding) {
                            coder.work(this, coding); // 在这里的this是产品，所以回调给产品，如果需要回调给boss这输入的则是boss
                        }
                    }.coding(s);
                }, "coder").start();
            }

            public void makeBigDeals(String bigDeal) {
                log.info("老板：将任务交给产品");
                new Thread(() -> {
                    this.productManager.work(this, bigDeal);
                }, "Product").start();
            }
        }.makeBigDeals("一个大项目");
        log.info("老板：下班回家");
        countDownLatch.await();
    }

    @Test
    public void reactive() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.defer(() -> {
                    log.info("老板：将任务交给产品");
                    return Mono.just("项目");
                })
                .publishOn(Schedulers.newSingle("Product"))
                .map(s -> {
                    log.info("产品经理：开始工作");
                    String midResult = "设计(" + s + ")";
                    log.info("产品经理：处理任务并给出原型: " + midResult);
                    log.info("产品经理：将任务交给程序员");
                    return midResult;
                })
                .publishOn(Schedulers.newSingle("Coder"))
                .map(s-> {
                    log.info("程序员：开始工作");
                    String result = "编程(" + s + ")";
                    log.info("程序员：完成任务{}", result);
                    return result;
                }).subscribe(result -> {
                    System.out.println("项目完成：" + result);
                    countDownLatch.countDown();
                });
        log.info("老板：下班回家");
        countDownLatch.await();

    }

}
