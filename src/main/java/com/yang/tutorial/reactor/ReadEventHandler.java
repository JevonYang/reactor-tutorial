package com.yang.tutorial.reactor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReadEventHandler extends EventHandler {

    private Selector selector;

    public ReadEventHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void handle(Event event) {
        //处理Accept的event事件
        if (event.getType() == EventType.READ) {
            log.info("事件类型：{} 处理事件：{}", event.getType(), event.getSource().toString());
        }
    }

}
