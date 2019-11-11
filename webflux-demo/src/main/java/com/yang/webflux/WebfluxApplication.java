package com.yang.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

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
}
