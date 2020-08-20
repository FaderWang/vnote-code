package com.fader.vnote.algorithm.base;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author Faderw
 * 冒泡排序
 * 原理：每一次排序选择最大的数放大末尾，通过相邻两数比较，进行交换，最大的数将会被交换到末尾。
 * 下次排序从剩余中选最大的，依次类推，直到数组有序。
 */
public class BubbleSort {

    static void bubbleSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            for (int j = 0; j < nums.length-i; j++) {
                // swap
                if (nums[j] > nums[j+1]) {
                    int temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] num = IntStream.of(5,20,89,45,33,10).toArray();
        bubbleSort(num);
        System.out.print(Arrays.toString(num));
    }
}
