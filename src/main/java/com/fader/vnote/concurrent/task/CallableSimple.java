package com.fader.vnote.concurrent.task;

import java.util.concurrent.*;

/**
 * @author FaderW
 * 2019/4/17
 */

public class CallableSimple {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);


    /**
     * 使用future接受callable结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void future() throws ExecutionException, InterruptedException {
        Future future = executorService.submit(new CallableImpl());
        if (!future.isCancelled()) {
            System.out.println(future.get());
        }
    }

    public static void futureTask() throws ExecutionException, InterruptedException {
        /**
         * 使用callable实例构造一个futureTask,futureTask继承runnable，可以当成任务执行
         * 同时是future的子类，可以获取执行的结果
         */
        FutureTask<String> futureTask = new FutureTask<>(new CallableImpl());
        executorService.submit(futureTask);
        System.out.println(futureTask.get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> callable = () -> {
            System.out.println("执行callable");
            return "执行callable";
        };
//        Future<String> future = executorService.submit(callable);
        FutureTask<String> futureTask = new FutureTask<String>(callable);
        executorService.submit(futureTask);
        System.out.println(futureTask.get());
    }

    static class CallableImpl implements Callable<String> {

        @Override
        public String call() throws Exception {
            return "执行callable";
        }
    }
}
