package com.fader.vnote.framework.interceptor;

/**
 * @author FaderW
 * @Date 2021/1/26 11:17
 */
public class LogInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String msg = (String) invocation.getArgs()[0];
        Object target = invocation.getTarget();
        System.out.println("LogInterceptor arg : " + msg + " target : " + target);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return AopProxy.wrap(target, this);
    }
}
