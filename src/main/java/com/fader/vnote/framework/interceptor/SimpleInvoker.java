package com.fader.vnote.framework.interceptor;

/**
 * @author FaderW
 * @Date 2021/1/26 11:13
 */
public class SimpleInvoker implements Invoker{

    public static void main(String[] args) {
        InvokerFactory invokerFactory = new InvokerFactory();
        invokerFactory.addInterceptor(new LogInterceptor());
        invokerFactory.addInterceptor(new TimeInterceptor());
        Invoker invoker = invokerFactory.newInvoker();
        invoker.printInvoker("Mybatis");

    }

    @Override
    public void printInvoker(String msg) {
        System.out.println("print msg : " + msg);
    }
}
