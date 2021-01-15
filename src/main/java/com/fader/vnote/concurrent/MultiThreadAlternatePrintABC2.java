package com.fader.vnote.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FaderW
 * @Date 2021/1/15 14:16
 */
public class MultiThreadAlternatePrintABC2 {

    private final ReentrantLock mainLock = new ReentrantLock();
    private Condition conditionA = mainLock.newCondition();
    private Condition conditionB = mainLock.newCondition();
    private Condition conditionC = mainLock.newCondition();
    private int state;

    public void print(PrintTask printTask) {
        for (int i = 0; i < 10; i++) {
            mainLock.lock();
            try {
                while (state%3 != printTask.index) {
                    printTask.wait.await();
                }
                System.out.println(printTask.word);
                state++;
                printTask.notify.signalAll();
                printTask.wait.await();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mainLock.unlock();
            }
        }
    }

    public class PrintTask implements Runnable {
        int index;
        String word;
        Condition wait;
        Condition notify;

        public PrintTask(int index, String word, Condition wait, Condition notify) {
            this.index = index;
            this.word = word;
            this.wait = wait;
            this.notify = notify;
        }

        @Override
        public void run() {
            print(this);
        }
    }

    public void doTest() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(new PrintTask(0, "A", conditionA, conditionB));
        pool.submit(new PrintTask(1, "B", conditionB, conditionC));
        pool.submit(new PrintTask(2, "C", conditionC, conditionA));
    }

    public static void main(String[] args) {
        new MultiThreadAlternatePrintABC2().doTest();
    }
}
