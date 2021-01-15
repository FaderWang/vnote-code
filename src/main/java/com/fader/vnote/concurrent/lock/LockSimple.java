package com.fader.vnote.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author FaderW
 * 2020/2/14
 */

public class LockSimple {

    static void lockTest() {
        /**
         * reentrantLock独占锁流程
         * 1. 维护一个state共享资源，以及Node节点封装的线程等待队列
         * 2. 加锁调用轨迹：lock#reentrant-->lock#sync-->lock#nofairSync-->
         * 非公平锁：
         *  首先CAS更新锁资源，尝试获取锁，获取失败则执行acquire()方法获取锁
         *  tryAcquire获取锁，首先获取共享资源是否空闲状态，如果是，则直接获取锁
         *
         * 公平锁
         *  直接执行acquire获取锁，tryAcquire方法在共享资源空闲时，先判断前面是否有等待的线程，
         *  如果没有，获取锁，如果有的话，加入等待队列队尾排队。
         */
    }

    static void stampedLockTest() {
        // https://cloud.tencent.com/developer/article/1628994
        StampedLock stampedLock = new StampedLock();
        long stamp = stampedLock.tryOptimisticRead();
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {

            } finally {
                stampedLock.unlock(stamp);
            }
        }
    }

    public static void main(String[] args) {
        Thread.holdsLock("");
    }
}
