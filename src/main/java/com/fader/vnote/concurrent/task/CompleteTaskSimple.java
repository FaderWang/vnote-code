package com.fader.vnote.concurrent.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Assert;

import java.util.concurrent.*;

/**
 * @author FaderW
 * 2019/10/22
 */

public class CompleteTaskSimple {

    static void completeFutureExample() {
        // 设置一个预定义结果的completeFuture
        CompletableFuture completableFuture = CompletableFuture.completedFuture("msg");
        Assert.assertTrue(completableFuture.isDone());
        // getNow(null) future完成后返回结果，否则返回null
        Assert.assertEquals("msg", completableFuture.getNow(null));
    }


    /**
     * CompletableFuture的方法如果以Async结尾，它会异步的执行(没有指定executor的情况下)，
     * 异步执行通过ForkJoinPool实现，它使用守护线程去执行任务。注意这是CompletableFuture的特性，
     * 其它CompletionStage可以override这个默认的行为。
     */
    static void runAsyncExample() {
        CompletableFuture completableFuture = CompletableFuture.runAsync(() -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Assert.assertTrue(completableFuture.isDone());
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(completableFuture.isDone());
    }

    /**
     * 注意thenApply方法名称代表的行为。
     * then意味着这个阶段的动作发生当前的阶段正常完成之后。本例中，当前节点完成，返回字符串message。
     * Apply意味着返回的阶段将会对结果前一阶段的结果应用一个函数。
     * 函数的执行会被阻塞，这意味着getNow()只有打斜操作被完成后才返回。
     */
     static void thenApplyExample() {
        CompletableFuture cf = CompletableFuture.completedFuture("msg")
                .thenApply(s -> {
                    Assert.assertFalse(Thread.currentThread().isDaemon());
                    return s.toUpperCase();
                });

        Assert.assertEquals("msg", cf.getNow(null));
    }

    /**
     * 在前一个阶段完成后异步应用函数
     */
    static void thenApplyAsyncExample() {
        CompletableFuture cf = CompletableFuture.completedFuture("msg")
                .thenApplyAsync(s -> {
                    Assert.assertTrue(Thread.currentThread().isDaemon());
                    return s.toUpperCase();
                });

        Assert.assertNull(cf.getNow(null));
        Assert.assertEquals("MSG", cf.join());

    }

    /**
     * 如果下一阶段接收了当前阶段的结果，但是在计算的时候不需要返回值(它的返回类型是void)，
     * 那么它可以不应用一个函数，而是一个消费者， 调用方法也变成了thenAccept
     */
    static void thenAcceptExample() {
        StringBuilder stringBuilder = new StringBuilder();
        CompletableFuture cf = CompletableFuture.completedFuture("msg")
                .thenAccept(s -> stringBuilder.append(s));

        Assert.assertTrue(stringBuilder.length() > 0);
    }

    static void completeExceptionallyExample() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("cusotm-executor-%d").build();
        ExecutorService executor = new ThreadPoolExecutor(3, 3, 60L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue(1024), factory, new ThreadPoolExecutor.AbortPolicy());
        CompletableFuture cf = CompletableFuture.completedFuture("msg")
                .thenApplyAsync(String::toUpperCase, executor);

        CompletableFuture handler = cf.handle((s, t) ->
            t != null ? "msg upon cancel" : "");

        cf.completeExceptionally(new RuntimeException("complete exceptionally"));

//        Assert.assertTrue("Was not completed exceptionally", cf.isCompletedExceptionally());
        try {
            cf.join();
            System.out.println("should have throw an exception");
        } catch (CompletionException ex) {
            Assert.assertEquals("complete exceptionally", ex.getCause().getMessage());
        }

        Assert.assertEquals("msg upon cancel", handler.join());
    }

    static void thenCompose() {
        // 组合两个future, 第一个future的结果作为第二个future的入参，并返回新的future
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<String> future = completableFuture.thenApply(integer -> integer + "s")
                .thenCompose(f -> CompletableFuture.supplyAsync(() -> f + "200"));
        System.out.println(future.join());

    }





    public static void main(String[] args) {
//        completeFutureExample();
//        runAsyncExample();
//        thenApplyExample();
//        thenApplyAsyncExample();
//        thenAcceptExample();
//        completeExceptionallyExample();
        thenCompose();
    }

}
