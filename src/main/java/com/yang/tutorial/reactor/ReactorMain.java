package com.yang.tutorial.reactor;

import com.yang.tutorial.service.ThreadPoolService;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

/**
 * @author yangzijing
 */
@Slf4j
public class ReactorMain {

    public static void main(String[] args) {
        ExecutorService es = ThreadPoolService.getInstance("eventloop-pool-%d");
        Selector selector = new Selector();
        Dispatcher eventLooper = new Dispatcher(selector, es);
        Acceptor acceptor = new Acceptor(selector, 1000);

        eventLooper.registEventHandler(EventType.ACCEPT, new AcceptEventHandler(selector));
        eventLooper.registEventHandler(EventType.READ, new ReadEventHandler(selector));

        es.submit(acceptor);
        es.submit(eventLooper::handleEvents);
        Random r = new Random(1);
        while (true) {
            Scanner input=new Scanner(System.in);
            System.out.println("请输入信息：");
            String content = input.next();
            acceptor.addNewConnection(new InputSource("事件[" + content + "]", r.nextInt()));
        }

    }
}
