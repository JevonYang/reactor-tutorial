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
    public Mono<String> hello () throws InterruptedException {
        return redisTemplate.opsForValue()
                .get("key").log();
    }

    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public Mono<String> hello1() {
        return Mono.just("hello, world");
    }

    // wrk -t12 -c400 -d30s http://127.0.0.1:8080/index.html
    // -c, --connections: total number of HTTP connections to keep open with
    //                   each thread handling N = connections/threads
    //
    //-d, --duration:    duration of the test, e.g. 2s, 2m, 2h
    //
    //-t, --threads:     total number of threads to use
    //
    //-s, --script:      LuaJIT script, see SCRIPTING
    //
    //-H, --header:      HTTP header to add to request, e.g. "User-Agent: wrk"
    //
    //    --latency:     print detailed latency statistics
    //
    //    --timeout:     record a timeout if a response is not received within
    //                   this amount of time.

    //    sudo cp wrk /usr/local/bin

    // SSE：服务端推送（Server Send Event），在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，从HTML5开始加入。

    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public Flux<String> getCurrentTime() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    @GetMapping("/randomNumbers")
    public Flux<ServerSentEvent<Integer>> randomNumbers() {
        log.info("randomNumbers controller!");
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
                .map(data -> ServerSentEvent.<Integer>builder()
                        .event("random")
                        .id(Long.toString(data.getT1()))
                        .data(data.getT2())
                        .build());
    }

}
