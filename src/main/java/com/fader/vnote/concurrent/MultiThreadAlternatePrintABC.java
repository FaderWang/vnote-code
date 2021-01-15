package com.fader.vnote.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FaderW
 * @Date 2021/1/15 10:09
 * 三个线程交替打印ABC
 */
public class MultiThreadAlternatePrintABC {

    private int state;
    private final ReentrantLock reentrantLock;
    private final Object object = new Object();

    public MultiThreadAlternatePrintABC(int state) {
        this.state = state;
        this.reentrantLock = new ReentrantLock();
    }

    /**
     * 每个线程只能打印对应的字符
     * @param nums
     * @param word
     */
    public void printWord(int nums, String word) {
        while (true) {
            // 需要严格保证打印的顺序，所以必须加锁，且同时只有一个线程能修改state共享变量
            reentrantLock.lock();
            try {
                if (state%3 == nums) {
                    System.out.print(word);
                    state++;
                }
            } finally {
                reentrantLock.unlock();
            }
            try {
                TimeUnit.MILLISECONDS.sleep(200L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printWordWait(int nums, String word) {
        while (true) {
            try {
                synchronized (object) {
                    if (state%3 == nums) {
                        System.out.print(word);
                        state++;
                        object.notifyAll();
                    } else {
                        object.wait();
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MultiThreadAlternatePrintABC printABC = new MultiThreadAlternatePrintABC(0);
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(() -> printABC.printWordWait(0, "A"));
        pool.submit(() -> printABC.printWordWait(1, "B"));
        pool.submit(() -> printABC.printWordWait(2, "C"));
    }

}
