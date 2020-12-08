package com.fader.vnote.spring.ioc.cycle;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author FaderW
 * @Date 2020/12/8 14:48
 */
@Component
@Aspect
public class LogAspect {

    @Pointcut("@annotation(Log)")
    public void logPointcut(){};


    @Before("logPointcut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("before log");
    }
}
