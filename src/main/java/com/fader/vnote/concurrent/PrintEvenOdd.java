package com.fader.vnote.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FaderW
 * @Date 2021/1/15 11:16
 * 交替打印奇偶
 */
public class PrintEvenOdd {

    private final Object monitor = new Object();
    private final int times;
    private int state;
    public PrintEvenOdd(int times, int state) {
        this.times = times;
        this.state = state;
    }

    public void print() {
        for (int i = 0; i < times; i++) {
            synchronized (monitor) {
                try {
                    System.out.println(String.format("打印数值%d, 当前线程%s", state++, Thread.currentThread().getName()));
                    monitor.notifyAll();
                    monitor.wait();
                } catch (Exception e) {

                }
                // 防止子线程阻塞导致主线程不能退出
                monitor.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        PrintEvenOdd printEvenOdd = new PrintEvenOdd(10, 0);
        pool.submit(printEvenOdd::print);
        pool.submit(printEvenOdd::print);
    }
}
