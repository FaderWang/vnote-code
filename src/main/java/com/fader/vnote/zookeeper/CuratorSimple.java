package com.fader.vnote.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FaderW
 * 2019/7/17
 */

public class CuratorSimple {

    private AtomicInteger atomicInteger;
    private CuratorFramework client;

    public CuratorSimple(CuratorFramework client) {
        this.atomicInteger = new AtomicInteger();
        this.client = client;
    }


    public void test() throws Exception {
        synchronized (this) {
            if (atomicInteger.getAndIncrement() == 1) {
                client.getData().usingWatcher(new CuratorWatcher() {
                    @Override
                    public void process(WatchedEvent event) throws Exception {
                        if (Watcher.Event.EventType.NodeDeleted == event.getType()) {
                            System.out.println("Notify all");
                            System.out.println("This thread :" + Thread.currentThread().getName());
                            notifyFromWait();
                        }
                    }
                }).forPath("/test");
            }

            wait();
            System.out.println("Current thread has been notified : " + Thread.currentThread().getName());
        }
    }

    private synchronized void notifyFromWait() {
        notifyAll();
    }

    static class CuratorTask implements Runnable {

        private CuratorSimple curatorSimple;

        public CuratorTask(CuratorSimple curatorSimple) {
            this.curatorSimple =  curatorSimple;
        }

        @Override
        public void run() {
            try {
                curatorSimple.test();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();
        client.create().creatingParentsIfNeeded().forPath("/test");
        CuratorSimple curatorSimple = new CuratorSimple(client);
        CuratorTask curatorTask = new CuratorTask(curatorSimple);
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(curatorTask);
            thread.setName("thread" + i);
            pool.submit(thread);
        }

        Thread.sleep(10);
        client.delete().forPath("/test");
    }
}
