package com.fader.vnote.spring.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author FaderW
 * @Date 2020/12/22 14:03
 */
@Configuration
@ComponentScan(basePackages = "com.fader.vnote.spring.event")
public class EventConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(EventConfig.class);
        DemoEventPublisher publisher = applicationContext.getBean(DemoEventPublisher.class);
        publisher.publishDemoEvent("demoEvent");
    }
}
