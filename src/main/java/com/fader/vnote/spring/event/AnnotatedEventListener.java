package com.fader.vnote.spring.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author FaderW
 * @Date 2020/12/22 17:59
 */
@Component
public class AnnotatedEventListener {

    @EventListener(classes = DemoEvent.class)
    public void onDemoEvent(DemoEvent demoEvent) {
        System.out.println("receive event: " + demoEvent.getSource());
    }
}
