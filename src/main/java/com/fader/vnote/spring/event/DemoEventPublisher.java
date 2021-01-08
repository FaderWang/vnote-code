package com.fader.vnote.spring.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @author FaderW
 * @Date 2020/12/22 14:01
 */
@Component
public class DemoEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishDemoEvent(Object source) {
        applicationEventPublisher.publishEvent(new DemoEvent(source));
    }
}
