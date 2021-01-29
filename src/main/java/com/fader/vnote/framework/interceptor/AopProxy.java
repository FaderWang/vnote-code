package com.fader.vnote.framework.interceptor;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author FaderW
 * @Date 2021/1/26 10:22
 * 对同一个类进行代理，只会在内存中生成一个代理类，要实现不同的代理逻辑，每个代理类继承了Proxy类，
 * 持有一个InvocationHandler属性，生成代理类的时候传入不同的代理处理类就可以实现不同的代理逻辑
 */
public class AopProxy implements InvocationHandler {

    private Object target;

    private Interceptor interceptor;

    public AopProxy(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        Class<?> typeClass = target.getClass();
        Class<?>[] interfaces = typeClass.getInterfaces();
        Object proxy = Proxy.newProxyInstance(typeClass.getClassLoader(), interfaces, new AopProxy(target, interceptor));
        System.out.println("create proxy :" + proxy);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString") || method.getName().equals("equals") || method.getName().equals("hashcode")) {
            return method.invoke(target, args);
        }
//        System.out.println("代理：" + target.getClass().getName() + " hashcode :" + Objects.hashCode(target));
//        System.out.println("代理类：" + proxy);
        return interceptor.intercept(new Invocation(target, method, args));
    }

    public static void main(String[] args) {
        byte[] classByte = ProxyGenerator.generateProxyClass("InvokerProxy", SimpleInvoker.class.getInterfaces());
//        DynamicSubjectProxy proxyFactory = new DynamicSubjectProxy(target);
//        Subject proxy = (Subject) proxyFactory.getProxy();
//        proxy.drive();
        String path = Invoker.class.getResource("").getPath();

        try (FileOutputStream fileOutputStream = new FileOutputStream(path + "InvokerProxy.class")){
            fileOutputStream.write(classByte);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
