package com.fader.vnote.concurrent.base;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author FaderW
 * 2019/8/7
 */

public class ThreadPoolSimple {

    public static void createPool() {
//        //创建一个固定容量为10的线程池
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10,
//                60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            String name = "faderw";
            System.out.println(name);
        });
        new ThreadFactoryBuilder().setNameFormat("pool-%d").build();
//        Executors.newFixedThreadPool(1);
//        Executors.newSingleThreadExecutor();
//        Executors.newScheduledThreadPool(4);

        //优雅终止
        executorService.shutdown();
        executorService.shutdownNow();

    }

    public static void createWithGuava() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("simple-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10,
                60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor.shutdownNow();
        threadPoolExecutor.shutdown();
    }

    public static void usecase() throws IOException {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("simple-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1,
                60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                threadFactory, new ThreadPoolExecutor.AbortPolicy());

        ServerSocket serverSocket = new ServerSocket(20000);
        while (!threadPoolExecutor.isShutdown()) {
            Socket client = serverSocket.accept();
            threadPoolExecutor.execute(() ->
                handle(client));
        }
    }

    public static void future() throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Future<String> future = pool.submit((Callable)() ->
             "Hello World"
        );
        System.out.println(future.get());
        System.out.println("main thread");
    }

    private static void handle(Socket client) {

    }

    public static void scheduledPool() {
        ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1, null, null);

        scheduledPool.schedule(() -> System.out.println("execute schedule task")
        , 2, TimeUnit.SECONDS);

        scheduledPool.shutdown();

    }

    public static void completionService() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<>(
                executorService, new ArrayBlockingQueue<>(10));

        completionService.submit((Callable) () -> "Hello, World");

        Future future = completionService.take();
        System.out.println(future.get());
        Thread.currentThread().interrupt();
        BlockingQueue queue = new ArrayBlockingQueue(1024);
        queue.put("");

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        try {
//            future();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        scheduledPool();

//        completionService();
        Map<String, String> map = new HashMap<>();
        map.put("name", "wangyuxin");
    }
}
