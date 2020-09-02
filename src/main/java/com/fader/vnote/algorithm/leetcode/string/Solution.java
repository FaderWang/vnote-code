package com.fader.vnote.algorithm.leetcode.string;

public class Solution {

    /**
     * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
     * @param haystack
     * @param needle
     * @return
     */
    public int strStr(String haystack, String needle) {
        if (needle == null) {
            return 0;
        }
        char[] source = haystack.toCharArray();
        char[] target = haystack.toCharArray();
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
            while (true) {
                if (source[j])
            }

        }

        return -1;


    }
}
