package com.yang.tutorial.reactor;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author yangzijing
 */
@Slf4j
public class ReactorMain {

    public static void main(String[] args) {
        ExecutorService es = ThreadPoolService.getInstance("event-pool-%d");
        Selector selector = new Selector();
        Dispatcher eventLooper = new Dispatcher(selector, es);
        Acceptor acceptor = new Acceptor(selector, 1000);

        eventLooper.registEventHandler(EventType.ACCEPT, new AcceptEventHandler(selector));
        eventLooper.registEventHandler(EventType.READ, new ReadEventHandler(selector));

        es.submit(acceptor);
        es.submit(eventLooper::handleEvents);

        for (int i = 0; i < 100; i++) {
            log.info("添加事件： {}", i);
            acceptor.addNewConnection(new InputSource("事件[" + i + "]", i));
        }

    }
}
