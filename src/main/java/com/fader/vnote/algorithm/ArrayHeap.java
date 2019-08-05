package com.fader.vnote.algorithm;

import java.util.*;

/**
 * 堆实现
 * @author FaderW
 * 2019/3/13
 */

public class ArrayHeap<E> {

    private static final int CAPACITY = 1 << 4;

    private Object[] items;
    private int currentSize;
    private Comparator<E> comparator;

    public ArrayHeap(int capacity, Comparator<E> comparator) {
        this.items = new Object[capacity];
        this.comparator = comparator;
    }

    public ArrayHeap(Collection<E> collection, Comparator<E> comparator) {
        this.items = new Object[CAPACITY];
        this.comparator = comparator;
        initFromCollection(collection);
    }

    private void initFromCollection(Collection<E> collection) {
        initElementsFromCollection(collection);
        heapify();
    }

    private void initElementsFromCollection(Collection<E> collection) {
        Object[] array = collection.toArray();
        if(array.getClass() != Object[].class) {
            Arrays.copyOf(array, array.length, Object[].class);
        }
        int len = array.length;
        if (len == 1 || comparator != null) {
            for (int i = 0; i < len; i++) {
                if (array[i] == null) {
                    throw new NullPointerException();
                }
            }
        }
        this.items = array;
        this.currentSize = array.length;
    }

    public void add(E e) {
        this.insert(e);
    }

    public void insert(E e) {
        if (e == null) {
            throw new RuntimeException();
        }
        int i = currentSize;
        currentSize++;
        if (i == 0) {
            items[0] = e;
        } else {
            siftUp(i, e);
        }
    }

    public E poll() {
        if (currentSize == 0) {
            return null;
        }
        int s = --currentSize;
        E e = (E) items[0];
        E x = (E) items[s];
        items[s] = null;
        if (s != 0) {
            siftDown(0, x);
        }

        return e;
    }

    private void siftUp(int k, E x) {
        while (k > 0) {
            int parent = (k-1) >>> 1;
            Object c = items[parent];
            if (comparator.compare((E) c, x) <= 0) {
                break;
            }
            items[k] = c;
            k = parent;
        }
        items[k] = x;
    }


    /**
     * 构建堆和删除堆都可以转换成向下堆化的过程
     * @param k
     * @param x
     */
    private void siftDown(int k, E x) {
        int half = currentSize >>> 1;
        while (k < half) {
            //非叶子节点继续循环
            int child = (k << 1) + 1;
            Object c = items[child];
            int right = child + 1;
            //找到左右子树较小的那个节点，跟父节点比较
            if (right < currentSize && comparator.compare((E) items[right], (E) c) < 0) {
                c = items[child = right];
            }
            //满足堆条件，停止堆化
            if (comparator.compare(x, (E) c) <= 0) {
                break;
            }
            //不满足，交换swap（parent, child）
            items[k] = c;
            k = child;
        }
        items[k] = x;
    }

    /**
     * 构建堆
     */
    private void heapify() {
        // 最后一个非叶子节点开始堆化
        // 最后一个非叶子节点下标等于 节点长度右移一位-1
        for (int i = (currentSize >>> 1) - 1; i >=0; i--) {
            siftDown(i, (E) items[i]);
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("kobe");
        list.add("james");
        list.add("curry");
        list.add("durant");
        ArrayHeap<String> heap = new ArrayHeap<String>(list, Comparator.naturalOrder());

        int len = heap.currentSize;
        for (int i = 0; i < len; i++) {
            System.out.println(heap.poll());
        }
    }
}
