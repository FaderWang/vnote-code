package com.fader.vnote.collection.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author FaderW
 * 2019/3/12
 */

public class SampleBlockingQueue<E> {

    private ReentrantLock lock;

    private Condition notFull;

    private Condition notEmpty;

    private Object[] items;

    private int putIndex, takeIndex;

    private static int shoe = 100;

    public void print() {
        System.out.println(shoe++);
        if (lock.tryLock()) {
            System.out.println("获取到了锁");
        }
    }
    /**
     * number of item
     */
    private int count;

    public SampleBlockingQueue(int capacity) {
        items = new Object[capacity];
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    public void put(E e) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (count == items.length) {
                notFull.await();
            }
            items[putIndex] = e;
            if (++putIndex == items.length) {
                putIndex = 0;
            }
            count++;
            notEmpty.signal();
        } catch (Exception ex) {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            if (count == 0) {
                notEmpty.await();
            }
            final Object[] items = this.items;
            E e = (E) items[takeIndex];
            items[takeIndex] = null;
            if (++takeIndex == items.length) {
                takeIndex = 0;
            }
            count--;
            notFull.signal();

            return e;
        } catch (Exception ex) {
            lock.unlock();
        }
        return null;
    }

    /**
     * 和Object await、notify的区别：
     * condition可以有多个等待队列
     *
     *  object的方法关键点在于 当前线程和对象的监听器，调用此方法，会释放当前线程对于此对象的监听器，当前线程也处于等待 阻塞状态,前提是当前线程已经获取了CPU的的监听器，
        condition await()  旨在当前线程的调度，与对象无关，调用此方法会是当前线程释放LOCK，并且当前线程会进行condition的等待队列，等待有其他condition调用dosignal（）
        会被唤醒，将当前线程重新放入同步队列中等待获取锁，

         两种在表现上有点相似，但是作用机制是有区别的
         1.object wait() 不能单独使用，必须是在synchronized 下才能使用，
         2.object wait()必须要通过Nodify()方法进行唤醒
         3.condition await() 必须是当前线程被排斥锁 lock 后,，获取到condition 后才能使用
         4.condition await() 必须通过 sign() 方法进行唤醒

         一个是基于对象监听器的同步方式，一个是基于 ASQ同步机制的同步方式
     */

}
