package com.fader.vnote.pattern.单例模式;

public enum EnumSingleton {
    INSTANCE;

    public static EnumSingleton getInstance() {
        return INSTANCE;
    } 
}