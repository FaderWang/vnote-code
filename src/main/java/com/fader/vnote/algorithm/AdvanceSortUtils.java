package com.fader.vnote.algorithm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author FaderW
 * 高阶排序算法
 * 2019/6/11
 */

public class AdvanceSortUtils {

    /**
     * 桶排序
     * @param array 数组
     * @param num bucket数量
     */
    public static void bucketSort(int[] array, int num) {
        int n = array.length;
        int max = 0, min = 0;
        for (int i = 0; i < n; i++) {
            max = Math.max(max, array[i]);
            min = Math.min(min, array[i]);
        }
        int k = (max-min)/num+1;
        ArrayList<ArrayList<Integer>> arrayBucket = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            arrayBucket.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            int index = (array[i]-min)/k;
            arrayBucket.get(index).add(array[i]);
        }
        for (int i = 0; i < num; i++) {
            ArrayList<Integer> bucket = arrayBucket.get(i);
            Collections.sort(bucket);
        }
        ArrayList<Integer> result = Lists.newArrayList();
        for (ArrayList arrayList : arrayBucket) {
            result.addAll(arrayList);
        }

        for (Integer integer : result) {
            System.out.print(integer + ",");
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[100];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(20);
        }
        bucketSort(array, 10);
    }
}
