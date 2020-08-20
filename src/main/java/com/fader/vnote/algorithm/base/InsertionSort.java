package com.fader.vnote.algorithm.base;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author FaderW
 * 快速排序
 */
public class InsertionSort {

    static void insertionSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            int cur = nums[i];
            int j = i-1;
            while (j >= 0) {
                if (cur < nums[j]) {
                    nums[j+1] = nums[j--];
                } else {
                    break;
                }
            }
            nums[j+1] = cur;
        }
    }

    public static void main(String[] args) {
        int[] num = IntStream.of(5,20,89,45,33,10).toArray();
        insertionSort(num);
        System.out.print(Arrays.toString(num));
    }
}
