package com.fader.vnote.algorithm.base;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author FaderW
 * 快速排序
 */
public class QuickSort {

    static void quickSort(int[] nums, int left, int right) {
        if (left >= right) {
            return;
        }
        int p = partition2(nums, left, right);
        quickSort(nums, left, p-1);
        quickSort(nums, p+1, right);
    }

    /**
     * 分区函数，选择一个pivot，将小于它的放在它前面，大于它的放在它后面，每一次分区后，pivot一定在正确的位置
     * @return 分区下标
     */
    static int partition(int[] nums, int left, int right) {
        int pivot = nums[right];
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (nums[i] <= pivot) {
                int temp = nums[i];
                nums[i] = nums[storeIndex];
                nums[storeIndex] = temp;
                storeIndex++;
            }
        }
        nums[right] = nums[storeIndex];
        nums[storeIndex] = pivot;

        return storeIndex;
    }

    /**
     * 分区函数：选择第一个数为基准，分别从右往左找小于基准，在从左往右找大于基准的，进行交换。
     * 当两个坐标相遇，则于基准交换。
     * @param nums
     * @param left
     * @param right
     * @return
     */
    static int partition2(int[] nums, int left, int right) {
        int l = left;
        int r = right;
        int pivot = nums[left];
        while (l < r) {
            while (l < r && nums[r] > pivot) {
                r--;
            }
            while (l < r && nums[l] <= pivot) {
                l++;
            }
            int temp = nums[l];
            nums[l] = nums[r];
            nums[r] = temp;

        }
        nums[left] = nums[l];
        nums[l] = pivot;
        return l;
    }

    public static void main(String[] args) {
        int[] num = IntStream.of(100,98,5,20,89,45,33,10,100,99,250,233).toArray();
        quickSort(num, 0, num.length-1);
        System.out.print(Arrays.toString(num));
    }
}
