package com.fader.vnote.javabase.proxy;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibSubjectProxy implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Cglib proxy init before drive");
        Object value = methodProxy.invokeSuper(o, objects);
        System.out.println("Cglib proxy destroy after drive");
        return value;
    }

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\test\\src\\main\\java\\org\\example\\proxy");
        RealSubject  proxy = (RealSubject) Enhancer.create(RealSubject.class, new CglibSubjectProxy());
        proxy.drive();
    }
}
