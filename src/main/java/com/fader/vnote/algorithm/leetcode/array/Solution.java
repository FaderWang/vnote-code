package com.fader.vnote.algorithm.leetcode.array;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author FaderW
 */
public class Solution {

    /**
     * 26.给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
     *
     * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        /** 双指针法 当nums[index] == nums[i]时递增i跳过重复项，当不等于时，index++，比较下一个数。*/
        if (null == nums || nums.length == 0) {
            return 0;
        }
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[index]) {
                index++;
                nums[index] = nums[i];
            }
        }
        return index+1;
    }

    /**
     * 27.给你一个数组 nums 和一个值 val，你需要 原地 移除所有数值等于 val 的元素，并返回移除后数组的新长度。
     * @param nums
     * @param val
     * @return
     */
    public int removeElement(int[] nums, int val) {
        if (nums == null || nums.length == 0) return 0;
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }

        return i;
    }

    /**
     *  第二种解放，当需要删除元素较少时适用。只需要很少的交换次数。
     */
    public int removeElement2(int[] nums, int val) {
        if (nums == null || nums.length == 0) return 0;
        int i = 0;
        int len = nums.length;
        while (i < len) {
            if (nums[i] == val) {
                nums[i] = nums[len-1];
                len--;
            } else {
                i++;
            }
        }
        return len;
    }

    /**
     * 给你两个有序整数数组 nums1 和 nums2，请你将 nums2 合并到 nums1 中，使 nums1 成为一个有序数组。
     * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n 。
     * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        /** 需要临时数组存储合并后的数据，同时遍历num1、num2，每次取较小的放入临时数组中*/
        int i = 0, j = 0;
        int index = 0;
        while (i < m && j < n) {
            if (nums1[i] <= nums2[j]) {
                nums1[index] = nums1[i];
                i++;
            } else {
                nums1[index] = nums2[j];
                j++;
            }
            index++;
        }
        while (i < m) {
            nums1[index++] = nums1[i++];
        }
        while (j < n) {
            nums1[index++] = nums2[j++];
        }
    }

    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        /** 利用num1的剩余空间，从后往前遍历*/
        int i = m-1, j = n-1;
        int index = m+n-1;
        while (i >= 0 && j >= 0) {
            if (nums1[i] < nums2[j]) {
                nums1[index--] = nums2[j--];
            } else {
                nums1[index--] = nums1[i--];
            }
        }
        System.arraycopy(nums2, 0, nums1, 0, j+1);
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = IntStream.of(0,5,1,20,2,56,34,4).toArray();
//        int index = solution.removeDuplicates(nums);
//        System.out.println("length:" + index);
//        for (int i : nums) {
//            System.out.print(i+ ",");
//        }
        solution.sort(nums);
        System.out.print(Arrays.toString(nums));


    }

    public void sort(int[] array) {
        /** 在插入操作时，从有序部分从后往前遍历，每当当前val<array[j]时，就将当前array[j]往后移一位，留出位置。 */
        if (array == null || array.length < 2) {
            return;
        }
//        for (int i = 1; i < array.length; i++) {
//            int j = i - 1;
//            int val = array[i];
//            while (j >= 0) {
//                if (val < array[j]) {
//                    array[j+1] = array[j--];
//                } else {
//                    break;
//                }
//            }
//            array[j+1] = val;
//        }
        /** 每一次排序找出一个最大的元素，放到末尾，排序len-1次
         * 且在每次排序过程中，如果没有交换，说明数组是有序的，则省略后续排序
         * */
        for (int i = 1; i < array.length; i++) {
            boolean exchange = false;
            for (int j = 0; j < array.length - i; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    exchange = true;
                }

            }
            if (!exchange) {
                break;
            }
        }
    }
}
