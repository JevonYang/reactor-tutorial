package com.yang.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuples;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@RestController
@Slf4j
public class WebfluxApplication {

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Mono<String> hello1() {
        return redisTemplate.opsForValue()
                .get("key");
    }

    @RequestMapping(value = "/hello-async", method = RequestMethod.GET)
    public Mono<String> hello () throws InterruptedException {
        return redisTemplate.opsForValue()
                .get("key")
                .publishOn(Schedulers.newElastic("async"))
                .map(s -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + " hello";
                });
    }

    // SSE：服务端推送（Server Send Event），
    // 在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，从HTML5开始加入。
    @GetMapping("/time")
    public Flux<ServerSentEvent<String>> time() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> Tuples.of(seq, new SimpleDateFormat("HH:mm:ss").format(new Date())))
                .map(data -> ServerSentEvent.<String>builder()
                        .event("time")
                        .id(Long.toString(data.getT1()))
                        .data(data.getT2())
                        .build());
    }

}
