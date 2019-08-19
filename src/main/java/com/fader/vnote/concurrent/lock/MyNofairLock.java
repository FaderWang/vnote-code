package com.fader.vnote.concurrent.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @author FaderW
 * 2019/8/9
 */

public class MyNofairLock {


    static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            int c = getState();
            if (c == 0) {
                //直接尝试CAS获取锁，不管前面是否有前置
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else if (Thread.currentThread() == getExclusiveOwnerThread()) {
                int nextc = c + arg;
                if (arg < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }

        private final boolean fairTryAcquire(int arg) {
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            } else if (Thread.currentThread() == getExclusiveOwnerThread()) {
                int nextc = c + arg;
                if (arg < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }
    }
}
