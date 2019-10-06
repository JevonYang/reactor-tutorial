package com.yang.tutorial.eventbus;

import io.lettuce.core.event.Event;
import io.lettuce.core.event.EventBus;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;

/**
 * Default implementation for an {@link EventBus}. Events are published using a {@link Scheduler}.
 *
 * From package io.lettuce.core.event.DefaultEventBus;
 *
 * @author Mark Paluch
 * @since 3.4
 */
public class ReactorEventBus implements EventBus {

    private final DirectProcessor<Event> bus;
    private final FluxSink<Event> sink;
    private final Scheduler scheduler;

    public ReactorEventBus(Scheduler scheduler) {
        this.bus = DirectProcessor.create();
        this.sink = bus.sink();
        this.scheduler = scheduler;
    }

    @Override
    public Flux<Event> get() {
        return bus.onBackpressureDrop().publishOn(scheduler);
    }

    @Override
    public void publish(Event event) {
        sink.next(event);
    }
}
