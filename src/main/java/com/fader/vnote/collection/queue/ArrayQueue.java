package com.fader.vnote.collection.queue;

import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author FaderW
 * @Date 2021/2/2 10:00
 */
public class ArrayQueue<E> extends AbstractQueue<E>
    implements Serializable {
    private static final long serialVersionUID = -6964819581450286644L;

    private final static int DEFAULT_CAPACITY = 16;

    private Object[] queue;

    /**
     * 头节点下标
     */
    private int head;

    /**
     * 尾节点下标
     */
    private int tail;


    public ArrayQueue(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("capacity less then zero");
        }
        queue = new Object[calculateCapacity(capacity)];
    }

    public ArrayQueue() {
        queue = new Object[DEFAULT_CAPACITY];
    }

    private int calculateCapacity(int minCapacity) {
        int initCapacity = DEFAULT_CAPACITY;
        if (minCapacity > initCapacity) {
            initCapacity = minCapacity;
            initCapacity |= initCapacity >> 1;
            initCapacity |= initCapacity >> 2;
            initCapacity |= initCapacity >> 4;
            initCapacity |= initCapacity >> 8;
            initCapacity |= initCapacity >> 16;
            initCapacity++;
        }

        return initCapacity;
    }

    /**
     * 扩容
     */
    private void doubleCapacity() {
        assert head == tail;
        int p = head;
        int n = queue.length;
        int r = n - p;
        int newCapacity = n << 1;
        if (newCapacity < 0) {
            throw new IllegalStateException("Sorry, queue to big");
        }
        Object[] newQueue = new Object[n];
        System.arraycopy(queue, p, newQueue, 0, r);
        System.arraycopy(queue, 0, newQueue, r, p);
        queue = newQueue;
        head = 0;
        tail = n;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    private class QueIterator implements Iterator<E> {

        private int cursor = head;

        private int fence = tail;

        @Override
        public boolean hasNext() {
            return cursor != fence;
        }

        @Override
        public E next() {
            if (cursor == fence) {
                throw new NoSuchElementException();
            }
            E result = (E) queue[cursor];
            if (fence != tail || result == null) {
                throw new ConcurrentModificationException();
            }
            cursor = (cursor + 1) & (queue.length - 1);
            return result;
        }
    }

    @Override
    public int size() {
        return (tail - head) & (queue.length - 1);
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        queue[tail] = e;
        if ((tail = (tail + 1) & (queue.length - 1)) == head) {
            doubleCapacity();
        }
        return true;
    }

    @Override
    public E poll() {
        E result = (E) queue[head];
        // 如果队列为空则返回null, head也不需要右移
        if (result == null) {
            return null;
        }
        queue[head] = null;
        head = (head + 1) & (queue.length - 1);

        return result;
    }

    @Override
    public E peek() {
        return null;
    }

    public static void main(String[] args) {
//        ArrayQueue<String> queue = new ArrayQueue<>();
//        for (int i = 0; i < 20; i++) {
//            queue.add("" + i);
//            System.out.println(queue.poll());
//        }
        System.out.println(-1 & 7);
    }
}
