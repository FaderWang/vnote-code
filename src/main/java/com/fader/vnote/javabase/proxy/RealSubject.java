package com.fader.vnote.javabase.proxy;

public class RealSubject implements Subject {
    @Override
    public void drive() {
        System.out.println("real subject drive");
    }
}
