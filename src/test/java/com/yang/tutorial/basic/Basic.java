package com.yang.tutorial.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.util.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

@Slf4j
public class Basic {

    @Test
    public void basic() {

        Flux.just("hello world")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        log.info("output: {}", s);
                        System.out.println(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("onCompleted");
                    }
                }, new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) {
                        log.info("Subscribe");
                    }
                });

    }

    @Test
    public void basic1() throws InterruptedException {

        CountDownLatch finishedLatch = new CountDownLatch(1);

        Mono.just("Hello world")
                .log()
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        log.info("subscription");
                    }

                    @Override
                    public void onNext(String s) {
                        log.info("onNext: {}", s);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        log.error("Throwable: ", throwable);
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                    }
                });

//        finishedLatch.await();
    }

    @Test
    public void basic2 () {

        Mono.fromCallable(System::currentTimeMillis)
                .repeat()
                .publishOn(Schedulers.single())
                .log("foo.bar")
                .flatMap(time ->
                                Mono.fromCallable(() -> { Thread.sleep(1000); return time; })
                                        .subscribeOn(Schedulers.parallel())
                        , 8) //maxConcurrency 8
                .subscribe(System.out::println);

    }

    @Test
    public void parallelFlux() {
        Mono.fromCallable(System::currentTimeMillis)
                .repeat()
                .parallel(8) //parallelism
                .runOn(Schedulers.parallel())
                .doOnNext( d -> System.out.println("I'm on thread "+Thread.currentThread().getName()) )
                .subscribe();
    }


    @Test
    public void lifeCycle() {
        Flux.just("hello world")
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) {
                        log.info("doOnSubscribe");
                    }
                })
                .doOnRequest(new LongConsumer() {
                    @Override
                    public void accept(long value) {
                        log.info("doOnRequest: {}", value);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        log.info("doOnNext: {}", s);
                    }
                })
                .doOnEach(new Consumer<Signal<String>>() {
                    @Override
                    public void accept(Signal<String> stringSignal) {
                        log.info("doOnEach: {}", stringSignal.get());
                    }
                })
                .doOnCancel(new Runnable() {
                    @Override
                    public void run() {
                        log.info("doOnCancel");
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        log.error("doOnError :", throwable);
                    }
                })
                .doOnComplete(new Runnable() {
                    @Override
                    public void run() {
                        log.info("doOnComplete");
                    }
                }).doOnTerminate(new Runnable() {
                    @Override
                    public void run() {
                        log.info("doOnTerminate");
                    }
                }).doFinally(new Consumer<SignalType>() {
                    @Override
                    public void accept(SignalType signalType) {
                        log.info("doFinally");
                    }
                }).doAfterTerminate(new Runnable() {
                    @Override
                    public void run() {
                        log.info("doAfterTerminate");
                    }
                })
                .log()
                .subscribe();
    }


}
