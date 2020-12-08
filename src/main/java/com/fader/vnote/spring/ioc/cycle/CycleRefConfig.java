package com.fader.vnote.spring.ioc.cycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author FaderW
 * @Date 2020/12/8 10:48
 */
@Configuration
//@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.fader.vnote.spring.ioc.cycle")
public class CycleRefConfig {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(CycleRefConfig.class);
        ServiceA serviceA = (ServiceA) applicationContext.getBean("serviceA");
        serviceA.execute();
        System.out.println(serviceA);
    }

}
