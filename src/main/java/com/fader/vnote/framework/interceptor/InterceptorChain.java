package com.fader.vnote.framework.interceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FaderW
 * @Date 2021/1/26 11:35
 */
public class InterceptorChain {

    private List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    /**
     * 应用拦截器，即生成代理对象
     * @return
     */
    public Object plugin(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }
}
