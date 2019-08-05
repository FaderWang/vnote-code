package com.fader.vnote.zookeeper;

import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;

/**
 * @author FaderW
 * 2019/7/18
 */

public class CuratorUtils {

    public static void main(String[] args) {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", policy);
        List<String> paths = Lists.newArrayList();
        paths.add("/lock1");
        InterProcessMultiLock lock = new InterProcessMultiLock(client, paths);
        try {
            lock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
