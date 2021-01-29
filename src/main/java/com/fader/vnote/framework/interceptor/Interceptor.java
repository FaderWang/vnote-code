package com.fader.vnote.framework.interceptor;

/**
 * @author FaderW
 * @Date 2021/1/26 10:17
 */
public interface Interceptor {

    /**
     * 拦截逻辑
     * @return
     * @throws Throwable
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 应用拦截器，返回一个代理对象
     * @param target
     * @return
     */
    Object plugin(Object target);
}
