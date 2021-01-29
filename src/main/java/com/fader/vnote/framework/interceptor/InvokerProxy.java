package com.fader.vnote.framework.interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author FaderW
 * @Date 2021/1/26 15:05
 */
public final class InvokerProxy extends Proxy implements Invoker {
    private static Method m1;
    private static Method m2;
    private static Method m3;
    private static Method m0;

    public InvokerProxy(InvocationHandler var1) {
        super(var1);
    }

    public final boolean equals(Object var1) {
        try {
            return (Boolean)super.h.invoke(this, m1, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }


    @Override
    public final void printInvoker(String var1) {
        try {
            super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m3 = Class.forName("com.fader.vnote.framework.interceptor.Invoker").getMethod("printInvoker", Class.forName("java.lang.String"));
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }

    public static void main(String[] args) {
        Invoker invoker = new InvokerFactory().newInvoker();
        // 第一层代理，代理处理类new AopProxy, target = invoker
        AopProxy logAopProxy = new AopProxy(invoker, new LogInterceptor());
        Invoker logInvoker = new InvokerProxy(logAopProxy);

        AopProxy timeAopProxy = new AopProxy(logInvoker, new TimeInterceptor());
        Invoker timeInvoker = new InvokerProxy(timeAopProxy);
        timeInvoker.printInvoker("Mybatis");
    }
}
