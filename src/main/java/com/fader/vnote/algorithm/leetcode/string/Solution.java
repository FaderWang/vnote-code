package com.fader.vnote.algorithm.leetcode.string;

import java.util.Arrays;

public class Solution {

    /**
     * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
     * 双指针解法， 复杂度O((m-n)n)
     * @param haystack
     * @param needle
     * @return
     */
    public static int strStr(String haystack, String needle) {
        if (needle.length() == 0 || haystack.length() == 0) {
            return 0;
    public int strStr(String haystack, String needle) {
//        if (needle.length() == 0) {
//            return 0;
//        }
//        if (haystack.length() == 0) {
//            return -1;
//        }
//        int sourceLen = haystack.length();
//        int targetLen = needle.length();
//
//        char first = needle.charAt(0);
//        int max = sourceLen - targetLen;
//        for (int i = 0; i <= max; i++) {
//            if (haystack.charAt(i) != first) {
//                while (++i <= max && haystack.charAt(i) != first);
//            }
//            if (i <= max) {
//                int j = i + 1;
//                int end = j + targetLen - 1;
//                for (int k = 1; j < end && haystack.charAt(j) == needle.charAt(k); j++, k++);
//
//                // 如果全部匹配，下标k到达末尾+1，即target.length
//                if (j == end) {
//                    return i;
//                }
//            }
//
//        }
//
//        return -1;

        // 借助jdk方法
        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
             if (haystack.substring(i, i+needle.length()).equals(needle)) {
                 return i;
             }
        }
        char[] source = haystack.toCharArray();
        char[] target = needle.toCharArray();
        char first = target[0];
        int max = source.length - target.length;
        for (int i = 0; i <= max; i++) {
            if (source[i] != first) {
                do {

                } while (++i <= max && source[i] != first);
            }
            if ((i == max && source[i] != first) ||
                i > max) {
                return -1;
            }
            int j = i+1;
            int k = 1;
            while (k < target.length) {
                if (source[j] == target[k]) {
                    j++;
                    k++;
                } else {
                    break;
                }
            }
            if (k == target.length) {
                return i;
        return -1;
    }

    /**
     * kmp匹配算法
     * @return
     */
    public static int kmpStr(String haystack, String needle) {
        int[] next = getNext(needle);
        int i = 0, j = 0;
        int max = haystack.length() - needle.length();
        while (i <= max && j < needle.length()) {
            if (j == -1 || haystack.charAt(i) == needle.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }

        if (j == needle.length()) {
            return i - j;
        }


        return -1;
    }

    private static int[] getNext(String pattern) {
        int len = pattern.length();
        int[] next = new int[len];
        next[0] = -1;
        int i = 1, j = 0;
        while (i < len - 1) {
            // j为-1说明第一个字母就不匹配，i+1向右移动一位， j置为0
            if (j == -1 || pattern.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }

        return next;
    public static void main(String[] args) {
        System.out.println(strStr("", ""));
    }

    public static void main(String[] args) {
//        int[] next = getNext("abababca");
//        System.out.print(Arrays.toString(next));
        System.out.print(kmpStr("hello", "ll"));
    }


}
