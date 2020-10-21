package com.fader.vnote.concurrent.threadpool;

import java.util.concurrent.*;

/**
 * @author FaderW
 */
public class Executor {

    public static void executeTask() {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        pool.execute(() -> {
            System.out.println("execute" + 5/0);
        });

        Future future = pool.submit(()-> {
            System.out.println("before");
            System.out.println("submit" + 5/0);
        });
        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pool.shutdown();
    }

    static class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
    }

    static class ExceptionThreadFactory implements  ThreadFactory{

        private static final ThreadFactory threadFactory = Executors.defaultThreadFactory();
        private static final Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionHandler();

        @Override
        public Thread newThread(Runnable r) {
            Thread t = threadFactory.newThread(r);
            t.setUncaughtExceptionHandler(exceptionHandler);
            return t;
        }
    }

    public static void main(String[] args) {
        executeTask();
    }
}
