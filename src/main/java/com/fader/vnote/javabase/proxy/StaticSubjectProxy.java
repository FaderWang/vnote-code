package com.fader.vnote.javabase.proxy;

/**
 * 静态代理
 * 静态代理需要实现代理对象的接口，不同的代理对象需要实现不同的接口，代理类不能复用
 */
public class StaticSubjectProxy implements Subject {

    private final Subject target;

    public StaticSubjectProxy(Subject target) {
        this.target = target;
    }

    @Override
    public void drive() {
        System.out.println("init before drive");
        target.drive();
        System.out.println("destroy after drive");
    }

    public static void main(String[] args) {
        Subject target = new RealSubject();
        Subject proxy = new StaticSubjectProxy(target);
        proxy.drive();
    }
}
