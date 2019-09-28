package com.yang.tutorial.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.yang.tutorial.model.DefaultUser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangz
 */
@Slf4j
public class GuavaEventBusDemo {

    @Subscribe
    public void sendMessage(String message) {
        log.info("发送了一条信息： {}", message);
        System.out.println("发送了一条信息： " + message);
    }

    @Subscribe
    public void sendInt(Integer message) {
        log.info("发送了一条信息： {}", message);
        System.out.println("发送了一条信息： " + message);
    }

    @Subscribe
    public void subScribeUser(DefaultUser user) {
        log.info("发送了一条信息： {}", user);
        System.out.println("发送了一条信息： " + user);
    }

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new GuavaEventBusDemo());

        eventBus.post("hello, world!");
        eventBus.post(1000);
        eventBus.post(new DefaultUser("hello", 18));

    }


}
