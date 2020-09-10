package com.fader.vnote.concurrent.task.work;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FaderW
 * 2020/2/25
 */

public class ExceptionThread extends Thread{

    public ExceptionThread(String name) {
        super(name);
        setUncaughtExceptionHandler(new ThreadExceptionHandler());
    }

    @Override
    public void run() {
        System.out.println(1/0);
    }

    static class ThreadExceptionHandler implements UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
//        ExceptionThread thread = new ExceptionThread("exceptionThread");
//        thread.start();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            int i = 1/0;
        });
        executorService.shutdown();
    }
}
