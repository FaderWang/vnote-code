package com.fader.vnote.framework.interceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author FaderW
 * @Date 2021/1/26 13:48
 */
public class TimeInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String msg = (String) invocation.getArgs()[0];
        Object target = invocation.getTarget();
        String timeStr = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("LogInterceptor time : " + timeStr + " target : " + target);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return AopProxy.wrap(target, this);
    }
}
