package com.fader.vnote.concurrent.multithreading;

/**
 * @author FaderW
 * 线程退出
 */
public class ThreadExit {

    static class StopFlagThread extends Thread {
        // 中断标志位
        private volatile boolean stop = false;

        public void close() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                System.out.print("execute task");
            }
        }
    }

    public static void stopFlag() {
        StopFlagThread thread = new StopFlagThread();
        thread.start();
        // 设置标识位为true，下次while循环时退出
        thread.close();
        thread.isInterrupted();
        Thread.interrupted();
        thread.interrupt();
    }

    public static void interrupt() {
        /**
         * 能够被中断的是轻量级阻塞，对应的线程状态是WAITING、TIMED_WAITING
         * 不能够被中断的是重量级阻塞，比如synchronized重量级锁，对应的线程状态是BLOCKED
         *
         */
    }

}
