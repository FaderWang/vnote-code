package com.fader.vnote.concurrent.atomic;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author FaderW
 * 2019/3/12
 */

public class AtomicCase {

//    private AtomicInteger count = new AtomicInteger();

//    public void add() {
//        int i = count.incrementAndGet();
//        System.out.println("current num : " + i);
//    }

    static void atomicIntegerUpdate() {
        AtomicInteger integer = new AtomicInteger(1);
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = integer.compareAndSet(1, 2);
            System.out.println("cur value : " + integer.get() + "isSuccess : " + success);
        }, "main thread").start();

        new Thread(() -> {
            integer.incrementAndGet();
            integer.decrementAndGet();
        }).start();
    }

    static void stampedUpdate() {
        AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(1, 1);
        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = stampedReference.compareAndSet(1, 2, 1, 2);
            System.out.println("cur value : " + stampedReference.getReference() + "isSuccess : " + success);
        }, "main thread").start();

        new Thread(() -> {
            stampedReference.compareAndSet(1, 2, 1, 2);
            stampedReference.compareAndSet(2, 1, 2, 3);
//            integer.decrementAndGet();
        }).start();
//        stampedReference.get()
    }

    public static void main(String[] args) {
//        atomicIntegerUpdate();
//        stampedUpdate();
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    private AtomicLong longCount = new AtomicLong();

    private AtomicBoolean atomicBoolean = new AtomicBoolean();


    public void add() {
        int i = count.incrementAndGet();
        System.out.println("current num : " + i);
    }

    public void decrement() {
        count.getAndDecrement();
    }

    public void longAdd() {
        longCount.incrementAndGet();
    }

    public void updateFlag() {
        atomicBoolean.compareAndSet(false, true);
    }



}
