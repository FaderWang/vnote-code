package com.fader.vnote.collection.queue;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

/**
 * @author FaderW
 * @Date 2021/2/2 15:16
 */
public class Heap {

    private int[] array;
    private int size;

    public Heap(int capacity) {
        this.array = new int[capacity];
    }

    public void insert(int i) {
        int index = size++;
        if (index == array.length) {
            throw new IllegalStateException("heap is full");
        }
        array[index] = i;
        // 自下往上堆化
        siftUp(index, i);
    }

    private void siftUp(int index, int i) {
        while (index > 0) {
            int parent = (index - 1) >>> 1;
            int c = array[parent];
            if (c <= i) {
                break;
            }
            array[index] = c;
            index = parent;
        }
        array[index] = i;
    }

    public int remove() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int result = array[0];
        size--;
        int x = array[size];
        siftDown(0, x);

        return result;
    }

    private void siftDown(int i, int x) {
        int half = size >>> 1;
        while (i < half) {
            int child = (i << 1) + 1;
            int right = child + 1;
            int c = array[child];
            if (right < size && array[right] < array[child]) {
                c = array[child = right];
            }
            if (x <= c) {
                break;
            }
            array[i] = c;
            i = child;
        }
        array[i] = x;
    }

    public static void heapSort(int[] array, int n) {
        buildHeap(array);
        while (n > 0) {
            int temp = array[n];
            array[n] = array[0];
            array[0] = temp;
            heapify(array, 0, n);
            n--;
        }
    }

    private static void buildHeap(int[] array) {
        int len = array.length;
        for (int i = (len >>> 1) - 1; i >= 0; i--) {
            heapify(array, i, len);
        }
    }

    /**
     * 堆化
     * @param array
     * @param i
     * @param len
     */
    private static void heapify(int[] array, int i, int len) {
        int half = len >>> 1;
        int x = array[i];
        while (i < half) {
            int child = (i << 1) + 1;
            int right = child + 1;
            int c = array[child];
            if (right < len && array[right] < array[child]) {
                c = array[child = right];
            }
            if (x <= c) {
                break;
            }
            array[i] = c;
            i = child;
        }
        array[i] = x;
    }


    public static void main(String[] args) {
        Heap heap = new Heap(20);
        int[] array = IntStream.of(0, 10, 2, 45, 19, 45, 23, 67, 444, 69, 4559, 47, 273, 239, 450, 3, 45, 47, 34)
                .toArray();
        Heap.heapSort(array, array.length - 1);
        IntStream.of(array).forEach(System.out::println);

    }
}
