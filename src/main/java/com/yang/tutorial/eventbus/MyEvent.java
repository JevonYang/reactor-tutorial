package com.yang.tutorial.eventbus;

import io.lettuce.core.event.Event;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author yangz
 */
@Data
@ToString
public class MyEvent implements Event {

    private long timestamp;

    private String context;

    public MyEvent(String context) {
        timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        this.context = context;
    }
}
