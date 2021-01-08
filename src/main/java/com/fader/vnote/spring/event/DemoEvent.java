package com.fader.vnote.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author FaderW
 * @Date 2020/12/22 13:53
 */
public class DemoEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DemoEvent(Object source) {
        super(source);
    }
}
