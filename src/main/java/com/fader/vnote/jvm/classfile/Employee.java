package com.fader.vnote.jvm.classfile;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

public class Employee {

    /** ”faderw“ 是文本字符串*/
    private String name = "faderw";

    /** final 修饰 ”20“ 作为字面量会保存到常量池*/
    private final int age = 20;

    private static int size = 0x0001;

    private final AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        Class cache = Integer.class.getDeclaredClasses()[0]; //1
//        Field myCache = cache.getDeclaredField("cache"); //2
//        myCache.setAccessible(true);//3
//
//        Integer[] newCache = (Integer[]) myCache.get(cache); //4
//        newCache[132] = newCache[133]; //5
//
//        int a = 2;
//        int b = a + a;
//        System.out.printf("%d + %d = %d", a, a, b); //
    }

    public String getName() {
        return this.name;
    }
}
