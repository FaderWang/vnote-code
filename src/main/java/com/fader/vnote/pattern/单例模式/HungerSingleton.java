package com.fader.vnote.pattern.单例模式;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FaderW
 * 饿汉式，在类加载阶段单例变量会被初始化
 * 并且初始化阶段是线程安全的
 * 2019/7/1
 */

public class HungerSingleton {


    private static HungerSingleton singleton;

    public static final String msg = "测试";

    static {
        singleton = new HungerSingleton();
    }



    private HungerSingleton() {
        System.out.println("实例化");
    }

    public static HungerSingleton getInstance() {
        return singleton;
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {
            service.execute(() -> {
                HungerSingleton singleton = getInstance();
                System.out.println(singleton);
            });
        }
    }

}
