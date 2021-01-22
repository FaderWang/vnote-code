package com.fader.vnote.spring.ioc.init;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author FaderW
 * @Date 2020/12/9 18:52
 */
@Configuration
@ComponentScan(basePackages = "com.fader.vnote.spring.ioc.init")
public class InitConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(InitConfig.class);
//        applicationContext.getBean("")
        applicationContext.getBean("initBean");
        applicationContext.getBean(InitBean.class);
//        InitBean initBean = new InitBean();
//
//        System.out.println(Object.class.isInstance(initBean));
    }
}
