package com.fader.vnote.javabase.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author FaderW
 * 2019/12/30
 */

public class HelloHandler implements InvocationHandler {

    private Object target;

    public HelloHandler(Object target) {
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("dynamic proxy hello");
        Object value = method.invoke(target, args);
        return value;
    }

    public static void main(String[] args) {
        Hello hello = new HelloImpl();
        HelloHandler proxy = new HelloHandler(hello);
        Hello proxyHello = (Hello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), proxy);
        proxyHello.sayHello();
    }
}
