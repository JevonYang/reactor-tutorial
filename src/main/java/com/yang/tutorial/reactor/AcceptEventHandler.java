package com.yang.tutorial.reactor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

/**
 * @author yangzijing
 */
@Slf4j
public class AcceptEventHandler extends EventHandler {

    private Selector selector;

    public AcceptEventHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void handle(Event event) {
        //处理Accept的event事件
        if (event.getType() == EventType.ACCEPT) {
            log.info("开始 - 事件类型：{} 处理事件：{} ", event.getType(), event.getSource().toString());
            //将事件状态改为下一个READ状态，并放入selector的缓冲队列中
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("结束 - 事件类型：{} 处理事件：{} ", event.getType(), event.getSource().toString());
            Event readEvent = new Event();
            readEvent.setSource(event.getSource());
            readEvent.setType(EventType.READ);
            selector.addEvent(readEvent);
        }
    }

}
