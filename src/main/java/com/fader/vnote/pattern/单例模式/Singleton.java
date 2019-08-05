package com.fader.vnote.pattern.单例模式;

/**
 * DCL双重加锁
 */
public class Singleton {

    /**
     * 这里加volatile不是为了保证可见性，synchronized关键字已经保证了可见性，
     * 这里是为了防止指令重排序。
     * instance = new instance 不是原子的，可分为三步：1.分配空间，2初始化对象，3.链接引用
     * volatile写之前插入StoreStore内存屏障，防止重排序。
     */
    private static volatile Singleton instance;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }


        return instance;
    }
}