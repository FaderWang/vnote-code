package com.fader.vnote.framework.interceptor;

/**
 * @author FaderW
 * @Date 2021/1/26 11:21
 */
public class InvokerFactory {

    private InterceptorChain interceptorChain = new InterceptorChain();

    public void addInterceptor(Interceptor interceptor) {
        this.interceptorChain.addInterceptor(interceptor);
    }

    public Invoker newInvoker() {
        Invoker invoker = new SimpleInvoker();
        invoker = (Invoker) interceptorChain.plugin(invoker);
        return invoker;
    }
}
