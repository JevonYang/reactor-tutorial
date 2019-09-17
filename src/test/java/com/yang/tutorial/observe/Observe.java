package com.yang.tutorial.observe;

import com.yang.tutorial.observable.Flow;
import com.yang.tutorial.observable.Speaker;
import com.yang.tutorial.observable.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Observe {

    @Test
    public void test() {

        // 被观察者
        Speaker speaker = new Speaker();
        // 观察者
        User user = new User("king");
        // 将观察者注册到观察者
        speaker.registerObserver(user);

        // 被观察者发生变化，观察者进行相应
        speaker.notifyObserver("hello");
    }

    @Test
    public void customFlow() {
        new Flow<String>()
                .create("hello1")
                .subscribe(item -> System.out.println(item + ", world"));
    }


}
