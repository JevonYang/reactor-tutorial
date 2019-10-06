package com.yang.tutorial.eventbus;

import reactor.core.scheduler.Schedulers;

/**
 * @author yangz
 */
public class ReactorEventBusDemo {

    public static void main(String[] args) {

        ReactorEventBus eventBus = new ReactorEventBus(Schedulers.elastic());

        eventBus.get().subscribe(event -> {
            System.out.println("被A组件订阅： " + event.toString() + " 运行线程： " + Thread.currentThread().getName());
        });

        eventBus.get().subscribe(event -> {
            System.out.println("            被B组件订阅： " + event.toString() + " 运行线程： " + Thread.currentThread().getName());
        });

        eventBus.publish(new MyEvent("hello, world"));

        eventBus.publish(new MyEvent("你好全世界"));

    }

}
