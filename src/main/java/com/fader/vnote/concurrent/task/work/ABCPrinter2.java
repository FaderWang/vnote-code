package com.fader.vnote.concurrent.task.work;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FaderW
 * 2019/12/1
 */

public class ABCPrinter2 implements Runnable {

    private static volatile boolean start = false;
    private static volatile int curCount;
    private static final int Count = 5;

    private String printChar;
    private String nextChar;
    private ReentrantLock lock;
    private Condition thisCondition;
    private Condition nextCondition;

    public ABCPrinter2(String printChar, String nextChar, ReentrantLock lock, Condition thisCondition, Condition nextCondition) {
        this.nextChar = nextChar;
        this.printChar = printChar;
        this.lock = lock;
        this.thisCondition = thisCondition;
        this.nextCondition = nextCondition;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            while (curCount < Count) {
                if (!start && !printChar.equals("A")) {
                    thisCondition.await();
                } else {
                    start = true;
                    System.out.println(printChar);
                    if (printChar.equals("C")) {
                        curCount++;
                    }
                    nextCondition.signal();
                    thisCondition.await();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition conditionA = reentrantLock.newCondition();
        Condition conditionB = reentrantLock.newCondition();
        Condition conditionC = reentrantLock.newCondition();
        Thread t1 = new Thread(new ABCPrinter2("A", "B", reentrantLock, conditionA, conditionB));
        Thread t2 = new Thread(new ABCPrinter2("B", "C", reentrantLock, conditionB, conditionC));
        Thread t3 = new Thread(new ABCPrinter2("C", "A", reentrantLock, conditionC, conditionA));
        t3.start();
        t2.start();
        t1.start();
    }
}
