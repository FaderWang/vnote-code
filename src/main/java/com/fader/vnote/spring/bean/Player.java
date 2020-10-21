package com.fader.vnote.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author FaderW
 */
@Component(value = "player")
public class Player implements BeanPostProcessor, ApplicationContextAware, BeanFactoryAware {

    private String name;

    public Player() {
        this.name = "curry";
        System.out.println("执行构造方法");
    }

    public void play() {
        System.out.println("my name is" + name);
    }

    @PostConstruct
    public void init() {
        System.out.println("执行init方法");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("执行destroy方法");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("执行setBeanFactory方法");
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
//        if (s.equals("player") || o instanceof Player) {
        System.out.println("执行postProcessBeforeInitialization方法");
//        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        System.out.println("执行postProcessAfterInitialization方法");
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("执行setApplicationContext方法");
    }
}
