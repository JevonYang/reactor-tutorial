package com.yang.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.ForkJoinPool;

@SpringBootApplication
@RestController
@Slf4j
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

    @GetMapping("/async-deferredresult")
    public DeferredResult<ResponseEntity<?>> handleReqDefResult(Model model) {
        log.info("Received async-deferredresult request");
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();

        ForkJoinPool.commonPool().submit(() -> {
            log.info("Processing in separate thread");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            output.setResult(ResponseEntity.ok("ok"));
        });

        log.info("servlet thread freed");
        return output;
    }

}
