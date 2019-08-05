package com.fader.vnote.algorithm;


import java.util.Random;

/**
 * @author FaderW
 * 2019/4/4
 */

public final class SortUtils {

    /**
     * 插入排序
     * 元素分为两部分，有序部分，和无序的待排序部分，每次从无序集中取出一个插入到合适的位置
     * @param array
     * @return
     */
    public static void insertionSort(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        for (int i = 1; i < array.length; i++) {
            int value = array[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (value < array[j]) {
                    array[j+1] = array[j];
                } else {
                    break;
                }
            }
            array[j+1] = value;
        }
    }

    /**
     * 冒泡排序
     * 每一次通过相邻的两个数比较
     * 直到将最大的那个数放到末尾
     * @param array
     */
    public static void bubbleSort(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        int length = array.length;
        for (int i = 0; i < length-1; i++) {
            boolean flag = false;
            for (int j = 0; j < length-i-1; j++) {
                if (array[j] > array[j+1]) {
                    int temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                    flag = true;
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 选择排序
     * 将数组分为两组有序和无序，每次从无序数组中选择最小元素放到有序末尾
     * @param array
     */
    public static void selectionSort(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }
        int length = array.length;
        for (int i = 0; i < length-1; i++) {
            int index = i;//当前最小数索引
            for (int j = i; j < length-1; j++) {
                if (array[j+1] < array[index]) {
                    index = j+1;
                }
            }
            if (index == i) {
                continue;
            }
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }

    /**
     * 快速排序
     * 采用分而治之的思想，先选择一个pivot元素，比它小的放前面，比它大的放后面
     * 然后拆分成多个子情况处理
     * @param array
     * @param n
     */
    public static void quickSort(int[] array, int n) {
        quickSort(array, 0, n-1);
    }

    private static void quickSort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int p = partition(array, left, right);
        quickSort(array, left, p-1);
        quickSort(array, p+1, right);

    }

    private static void quickSort2(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }
        int p = partition2(array, left, right);
        quickSort(array, left, p-1);
        quickSort(array, p+1, right);
    }

    /**
     * 分区处理，顺序遍历法。
     * 选取最后一个元素为pivot，维护一个storeIndex,从left到right遍历，
     * 每当元素小于pivot,存入当前storeIndex位置，storeIndex++,
     * 最后交换当前storeIndex与pivot,对左右两边递归处理
     * @return storeIndex
     */
    private static int partition(int[] array, int left, int right) {
        // 保存当前store下标，初始为left
        int storeIndex = left;
        int pivot = array[right];
        for (int j = left; j < right; j++) {
            // 如果小于pivot，说明应该存入当前storeIndex,交换位置
            if (array[j] < pivot) {
                int temp = array[j];
                array[j] = array[storeIndex];
                array[storeIndex] = temp;
                // storeIndex往后移动一位
                storeIndex++;
            }
        }

        // 最后将pivot于当前storeIndex元素交换
        int temp2 = array[storeIndex];
        array[storeIndex] = array[right];
        array[right] = temp2;

        return storeIndex;
    }

    /**
     * 分区处理，填坑法
     * 1.选择第一个元素作为pivot，从后往前遍历，找到小于pivot的元素，填入l
     * 2.从前往后遍历，找到大于pivot的元素，填入r
     * 3.知道l与r重合，返回下标
     * @return
     */
    private static int partition2(int[] array, int left, int right) {
        int l = left, r = right;
        int pivot = array[left];

        while (l < r) {
            while (l < r && array[r] > pivot) {
                 r--;
            }
            array[l] = array[r];
            while (l < r && array[l] <  pivot) {
                l++;
            }
            array[r] = array[l];
        }
        array[l] = pivot;
        return l;
    }

    public static void mergeSort(int[] array) {
        mergeSort(array, 0, array.length-1);
    }

    /**
     * 归并排序
     * 将数组从中间划分为两个子数组，递归调用此过程，直到无法划分，
     * 然后依次合并两个子数组
     * @param array
     * @param p
     * @param q
     */
    private static void mergeSort(int[] array, int p, int q) {
        if (p >= q) {
            return;
        }
        int mid = (p+q)/2;
        mergeSort(array, p, mid);
        mergeSort(array, mid+1, q);
        merge(array, p, q, mid);
    }

    private static void merge(int[] array, int p, int q, int mid) {
        //定义临时变量存储合并的有序数组
        int[] tempArr = new int[q-p+1];

        int i = p, j = mid+1;
        int tempIndex = 0;
        /*依次从两个数组取出元素比较，较小的放入当前tempArr[index],
            从较小的那个数组中再取出下一个进行比较，直到其中一个数组全部插入临时数组，
            将另一个数组剩下的直接插入临时数组
        */
        while (i <= mid && j <= q) {
            tempArr[tempIndex++] = array[i] > array[j]
                    ? array[j++] : array[i++];
        }
        while (i <= mid) {
            tempArr[tempIndex++]=array[i++];
        }
        while (j <= q) {
            tempArr[tempIndex++]=array[j++];
        }
        System.arraycopy(tempArr, 0, array, p, q-p+1);
    }

    public static void main(String[] args) {
//        int[] array = {10, 2, 5, 1, 9};
//        insertionSort(array);
//        bubbleSort(array);
//        selectionSort(array);
        int[] array = new int[10];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100);
        }
        mergeSort(array, 0, array.length-1);
        for (int i : array) {
            System.out.print(i + ",");
        }


    }
}
