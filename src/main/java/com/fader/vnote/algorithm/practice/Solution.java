package com.fader.vnote.algorithm.practice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 * 2019/5/22
 */

public class Solution {

    public static int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        for (int start = 0, end = 0; end < s.length();) {

            if (map.containsKey(s.charAt(end))) {
                start = Math.max(start, map.get(s.charAt(end)) + 1);
            }
            map.put(s.charAt(end), end);
            end++;
            max = Math.max(max, end - start);
        }
        return max;
    }

    public static void main(String[] args) {
//        System.out.println(lengthOfLongestSubstring("abba"));
        String s = "abcabcabc";
        System.out.println(s.indexOf('a', 4));
    }
}