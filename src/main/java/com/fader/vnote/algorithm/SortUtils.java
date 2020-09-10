package com.fader.vnote.algorithm;

import java.util.*;
import java.util.stream.IntStream;

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
        /** 在插入操作时，从有序部分从后往前遍历，每当当前val<array[j]时，就将当前array[j]往后移一位，留出位置。 */
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
     * 每一次排序找出一个最大的元素，放到末尾，排序len-1次
     * 且在每次排序过程中，如果没有交换，说明数组是有序的，则省略后续排序
     * @param array
     */
    public static void bubbleSort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        int length = array.length;
        for (int i = 0; i < length; i++) {
            boolean flag = false;
            for (int j = 0; j < length - i - 1; j++) {
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

    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        char[] sc = s.toCharArray();
        char[] tc = t.toCharArray();
        Arrays.sort(sc);
        Arrays.sort(tc);

        return new String(sc).equals(new String(tc));

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

    public static void mergeSort2(int[] array) {
        int n = array.length - 1;
        //步长
        int step = 1;
        int low;
        while(step <= n){
            low = 0;
            while(low + step <= n){
                int mid = low + step - 1;
                int high = mid + step;
                if (high > n) {
                    high = n;
                }
                merge(array,low,high, mid);
                low = high + 1;
            }
            //处理末尾残余部分
            step*=2;
        }
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

    static int[] queens = new int[8];

    public static void calculate8Queens(int row) {
        if (row == 8) {
            printQueens();
            return;
        }
        for (int col = 0; col < 8; col++) {
            if (isOk(row, col)) {
                queens[row] = col;
                calculate8Queens(row + 1);
            }
        }
    }

    private static boolean isOk(int row, int col) {
        int leftup = col - 1;
        int rightup = col + 1;
        for (int i = row - 1; i >= 0; i--) {
            if (queens[i] == col) {
                return false;
            }
            if (queens[i] == leftup) {
                return false;
            }
            if (queens[i] == rightup) {
                return false;
            }
            leftup--;
            rightup++;
        }
        return true;
    }

    private static void printQueens() {
        for (int i = 0; i < queens.length; i++) {
            for (int j = 0; j < queens[i]; j++) {
                System.out.print("*");
            }
            System.out.print(i);
            System.out.println();
        }
    }

    /**
     * 电话号码字母组合
     * 输入："23"
     * 输出：["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"].
     * @param digits
     * @return
     */
    public static List<String> letterCombinations(String digits) {
        if (digits == null || digits.isEmpty()) {
            return null;
        }
        combination("", digits);
        return res;
    }

    private static void combination(String combination, String digit) {
        if (digit == null || digit.isEmpty()) {
            res.add(combination);
            return;
        }
        String letter = phone.get(digit.substring(0, 1));
        for (int i = 0; i < letter.length(); i ++) {
            combination(combination + letter.charAt(i), digit.substring(1));
        }
    }

    static List<String> res = new ArrayList<>();
    static Map<String, String> phone = new HashMap<>(10);
    static {
        phone.put("2", "abc");
        phone.put("3", "def");
        phone.put("4", "ghi");
        phone.put("5", "jkl");
        phone.put("6", "mno");
        phone.put("7", "pqrs");
        phone.put("8", "tuv");
        phone.put("9", "wxyz");

    }

    public static List<String> generateParenthesis(int n) {
        if (n < 1) {
            return new ArrayList<>();
        }
        generate("", 0, 0, n);
        return res;
    }


    public static void generate(String generate, int leftCount, int rightCount, int n) {
        if (leftCount == n) {
            int left = n - rightCount;
            while (left > 0) {
                generate += ")";
                left--;
            }
            res.add(generate);
            return;
        }
        if (rightCount + 1 <= leftCount) {
            generate(generate + ")", leftCount, rightCount+1, n);
        }
        generate(generate+"(", leftCount+1, rightCount, n);
    }

    /**
     * 二分查找
     * @param a
     * @param n
     * @param val
     */
    public static int binarySerach(int[] a, int n, int val) {
        int low = 0;
        int high = n-1;
        while (low <= high) {
            int mid = high + (low - high) >> 1;
            if (a[mid] == val) {
                return mid;
            } else if (val < a[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -1;
    }



    public static void main(String[] args) {
//        int[] array = {10, 2, 5, 1, 9};
//        insertionSort(array);
////        bubbleSort(array);
////        selectionSort(array);
////        int[] array = new int[10];
////        Random random = new Random();
////        for (int i = 0; i < array.length; i++) {
////            array[i] = random.nextInt(100);
////        }
////        mergeSort2(array);
//        for (int i : array) {
//            System.out.print(i + ",");
//        }
        int[] a = IntStream.of(1,3,6,7,12,23,45,67,78,90,123,1345,2345).toArray();
        int val = 23;
        System.out.println(binarySerach(a, a.length, val));

//        calculate8Queens(0);

//        String s = "ss";
//        System.out.println(s.substring(3).isEmpty());
//        List<String> res = generateParenthesis(3);
//        System.out.println(res);
    }
}
