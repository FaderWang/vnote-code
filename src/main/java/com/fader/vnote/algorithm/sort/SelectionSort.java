package com.fader.vnote.algorithm.sort;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author FaderW
 * 选择排序
 */
public class SelectionSort {

    /**
     * 从每次排序选择一个最小数放到索引位置，索引从零开始。
     * 排序过程中数组分为已排序、未排序两部分。每次从未排序数组中选出最小的数的下标，放到当前索引位置（与当前索引位置的值交换），即已排序末尾。
     * @param nums
     */
    static void selectionSort(int[] nums) {
        for (int i = 0; i < nums.length-1; i++) {
            int index = i;
            for (int j = i+1; j < nums.length; j++) {
                if (nums[j] < nums[index]) {
                    index = j;
                }
            }
            // 如果index没变，则省去交换
            if (index == i) {
                continue;
            }
            int temp = nums[index];
            nums[index] = nums[i];
            nums[i] = temp;
        }
    }

    public static void main(String[] args) {
        int[] num = IntStream.of(5,20,89,45,33,10).toArray();
        selectionSort(num);
        System.out.print(Arrays.toString(num));
    }
}
