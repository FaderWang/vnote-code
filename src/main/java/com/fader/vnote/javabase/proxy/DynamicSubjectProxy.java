package com.fader.vnote.javabase.proxy;

import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 静态代理每个代理类型都需要编写相应的代理类，代理类不能复用
 */
public class DynamicSubjectProxy implements InvocationHandler {

    /**
     * 不与代理对象的类型耦合，可以代理任意类型的对象
     */
    private Object target;

    public DynamicSubjectProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("dynamic proxy init before drive");
        Object value = method.invoke(target, args);
        System.out.println("dynamic proxy destroy after drive");
        return value;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    public static void main(String[] args) {
        /**
         * JDK动态代理类在运行时计算生成，在 java.lang.reflect.Proxy 类中，
         * 就是用了 ProxyGenerator.generateProxyClass 来为特定接口生成形式为 *$Proxy 的代理类的二进制字节流
         */
//        Subject target = new RealSubject();
        // 根据传入的被代理类的接口以及定义的方法名，生成代理类的二进制字节流
        byte[] classByte = ProxyGenerator.generateProxyClass("SubjectProxy", RealSubject.class.getInterfaces());
//        DynamicSubjectProxy proxyFactory = new DynamicSubjectProxy(target);
//        Subject proxy = (Subject) proxyFactory.getProxy();
//        proxy.drive();
        String path = RealSubject.class.getResource("").getPath();

        try (FileOutputStream fileOutputStream = new FileOutputStream(path + "SubjectProxy.class")){
            fileOutputStream.write(classByte);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
