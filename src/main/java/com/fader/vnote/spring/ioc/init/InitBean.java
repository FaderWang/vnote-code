package com.fader.vnote.spring.ioc.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author FaderW
 * @Date 2020/12/9 17:56
 */
@Component
@Lazy
public class InitBean implements InitializingBean, BeanNameAware, ApplicationContextAware{

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Initializing Bean afterPropertiesSet执行");
    }

    public InitBean() {
        System.out.println("默认构造器执行");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct执行");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware setBeanName执行");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware setApplicationContext执行");
    }
}
