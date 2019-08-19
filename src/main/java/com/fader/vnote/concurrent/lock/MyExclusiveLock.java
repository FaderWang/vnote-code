package com.fader.vnote.concurrent.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author FaderW
 * 2019/8/9
 */

public class MyExclusiveLock {

    private final Sync sync;

    public MyExclusiveLock() {
        this.sync = new Sync();
    }

    public final void lock() {
        this.sync.acquire(1);
    }

    public final void unlock() {
        this.sync.release(1);
    }


    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else if (Thread.currentThread() == getExclusiveOwnerThread()) {
                int nextc = getState() + arg;
                if (c < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            int nextc = getState() - arg;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (nextc == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(nextc);
            return free;
        }
    }

    static class Task implements Runnable {
        private Integer count;
        private MyExclusiveLock lock = new MyExclusiveLock();

        @Override
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.lock();
            count++;
            System.out.println(count);
            lock.unlock();
        }

        public Task(Integer count) {
            this.count = count;
        }
    }

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        Task task = new Task(0);
        for (int i = 0; i < 100; i++) {
            pool.submit(new Thread(task));
        }
    }
}
