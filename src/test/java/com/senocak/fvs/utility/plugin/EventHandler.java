package com.senocak.fvs.utility.plugin;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventHandler implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, startedEventHandler);
        eventPublisher.registerHandlerFor(TestRunFinished.class, event -> report());
    }

    private final io.cucumber.plugin.event.EventHandler<TestRunStarted> startedEventHandler = event -> {
        log.debug("Test run started");
    };

    private void report(){
        log.debug("Test run finished");
    }
}
