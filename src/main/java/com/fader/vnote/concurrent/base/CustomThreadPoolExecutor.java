package com.fader.vnote.concurrent.base;


import java.util.concurrent.*;

/**
 * @author FaderW
 * 2019/10/8
 */

public class CustomThreadPoolExecutor extends ThreadPoolExecutor{


    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (null != t) {
            t.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
//        ThreadFactory threadFactory = new ThreadFactoryBuilder()
//                .setNameFormat("pool-%d").build();
//        ExecutorService executorService = new CustomThreadPoolExecutor(5, 5,
//                10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024),
//                threadFactory, new ThreadPoolExecutor.AbortPolicy());
//
//
//        executorService.execute(new Task());

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new Task());


    }

    static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("execute task");
//            throw new RuntimeException("throw new error");
            int i = 1/0;
        }
    }
}
