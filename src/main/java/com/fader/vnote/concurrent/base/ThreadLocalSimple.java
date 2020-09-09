package com.fader.vnote.concurrent.base;

import java.lang.reflect.Field;

public class ThreadLocalSimple {

    private static void test(String s, boolean isGC) {
        try {
            new ThreadLocal<>().set(s);
            if (isGC) {
                System.gc();
            }
            Thread t = Thread.currentThread();
            Class<? extends Thread> classZ = t.getClass();
            Field field = classZ.getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object threadLocalMap = field.get(t);
            Class<?> theadLocalMapClass = threadLocalMap.getClass();
            Field tableField = theadLocalMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] array = (Object[]) tableField.get(threadLocalMap);
            for (Object obj : array) {
                if (obj != null) {
                    Class<?> entryClass = obj.getClass();
                    Field valueField = entryClass.getDeclaredField("value");
                    Field referentField = entryClass.getSuperclass().getSuperclass().getDeclaredField("referent");
                    valueField.setAccessible(true);
                    referentField.setAccessible(true);
                    System.out.println(String.format("弱引用key:%s，值value:%s", referentField.get(obj), valueField.get(obj)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            test("abc", false);
        });
        t1.start();
        t1.join();

        Thread t2 = new Thread(() -> {
            test("def", true);
        });
        t2.start();
        t2.join();

        // ThreadLocal内存泄漏：https://zhuanlan.zhihu.com/p/56214714

    }
}
