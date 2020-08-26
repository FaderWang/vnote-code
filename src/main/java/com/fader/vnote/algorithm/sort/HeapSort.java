package com.fader.vnote.algorithm.sort;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author 堆排序
 */
public class HeapSort {

    static void heapSort(int[] array) {
        // 构建堆
        for (int i = (array.length >>> 1) - 1; i >= 0; i--) {
            siftDown(array, array[i], i, array.length);
        }
        for (int i = array.length - 1; i > 0; i--) {
            swap(array, 0, i);
            siftDown(array, array[0], 0, i);
        }

    }

    static void swap(int[] array, int m, int n) {
        int temp = array[m];
        array[m] = array[n];
        array[n] = temp;
    }


    /**
     *
     * @param array
     * @param e 待结构调整叶子对象
     * @param i 待结构调整叶子坐标
     * @param length
     */
    static void siftDown(int[] array, int e, int i, int length) {
        int half = length >>> 1;
        while (i < half) {
            int child = (i << 1) + 1;
            int c = array[child];
            int right = child + 1;
            if (right < length && array[right] > c) {
                c = array[child = right];
            }
            if (e >= c) {
                break;
            }
            array[i] = c;
            i = child;
        }
        array[i] = e;
    }

    public static void main(String[] args) {
        int[] num = IntStream.of(100,98,5,20,89,45,33,10,100,99,250,233).toArray();
        heapSort(num);
        System.out.print(Arrays.toString(num));
    }

}
