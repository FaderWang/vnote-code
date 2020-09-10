package com.fader.vnote.zookeeper;

import lombok.Getter;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author FaderW
 * ZooKeeper分布式锁
 *
 * 2019/7/17
 */

public class ZooLock {

    private String zkQurom = "localhost:2181";
    private String lockNameSpace = "/lock";

    private ThreadLocal<String> znode = new ThreadLocal<>();

    private ZooKeeper zooKeeper;

    public ZooLock() {
        try {
            zooKeeper = new ZooKeeper(zkQurom, 50000, watchedEvent -> {
                System.out.println("Receive event: " + watchedEvent);
                if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                    System.out.println("connection is established...");
                }
            });
            initRootPath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lock(String lockName) {
        if (!tryLock(lockName)) {
            waitforlock();
        }
        System.out.println("succss get lock:" + lockName);
    }

    public void unLock() {
        String znodeName = znode.get();
        try {
            //-1代表基于最新版
            zooKeeper.delete(znodeName, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    private void initRootPath() {
        try {
            Stat stat = zooKeeper.exists(lockNameSpace, false);
            if (stat == null) {
                zooKeeper.create(lockNameSpace, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void waitforlock() {
        CountDownLatch latch = new CountDownLatch(1);
        String znodeName = znode.get();
        Assert.notNull(znodeName, "Current thread znode must not null");
        try {
            List<String> childs = zooKeeper.getChildren(lockNameSpace, null);
            Collections.sort(childs);
            int currentIndex = childs.indexOf(znodeName.split("/")[2]);
            if (currentIndex == 0) {
                System.out.println("current index=0");
                latch.countDown();
            } else {
                String preZnodeName = childs.get(currentIndex - 1);
                Stat stat = zooKeeper.exists(lockNameSpace+ "/" + preZnodeName, watchedEvent -> {
                    if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
                        System.out.println("preNode has been deleted");
                        latch.countDown();
                    }
                });
                if (stat == null) {
                    System.out.println("pre stats = null");
                    return;
                }
            }
            latch.await();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean tryLock(String lockName) {
        String lockPath = lockNameSpace + "/" + lockName;
        String znodeName;
        try {
            znodeName = zooKeeper.create(lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            znode.set(znodeName);
            //判断是否是最小
            List<String> childs= zooKeeper.getChildren(lockNameSpace, false);
            System.out.println(childs.get(childs.size()-1));
            Collections.sort(childs);
            //如果是最小的，则获取锁成功
            if (znodeName.equals(childs.get(0))) {
                System.out.println("this is min");
                return true;
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    static class TicketTask implements Runnable {

        @Getter
        private Integer ticket = 0;

        private ZooLock zooLock = new ZooLock();

        private CountDownLatch countDownLatch;

        public TicketTask(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            zooLock.lock("ticket1");
            ticket++;
            zooLock.unLock();
            countDownLatch.countDown();
        }
    }


    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        CountDownLatch countDownLatch = new CountDownLatch(100);
//        int ticket = 100;
//        ZooLock zooLock = new ZooLock();
        TicketTask task = new TicketTask(countDownLatch);
        for (int i = 0; i < 10; i++) {
            pool.submit(task);
        }

        countDownLatch.await();
        System.out.println(task.getTicket());
//        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, watchedEvent -> {
//            System.out.println("Receive event: " + watchedEvent);
//            if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
//                System.out.println("connection is established...");
//            }
//        });
//        List<String> childs = zooKeeper.getChildren("/lock", false);
//        childs.forEach(s -> {
//            try {
//                System.out.println(s);
//                zooKeeper.delete("/lock/" + s, -1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (KeeperException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
