package com.fader.vnote.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author FaderW
 * 2019/7/16
 */

public class ZookSimple implements Watcher{
    private static CountDownLatch downLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookSimple());
//        long sessionId = zooKeeper.getSessionId();
//        byte[] passwd = zooKeeper.getSessionPasswd();
//        System.out.println("sessionId:" + sessionId + ",passwd:" + new String(passwd));
        String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println("Success create znode: " + path1);
        System.out.println("Success create znode: " + path2);
        downLatch.await();

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            downLatch.countDown();
        }
    }
}
