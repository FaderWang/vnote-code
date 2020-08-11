package com.fader.vnote.algorithm.leetcode;

/**
 * @author FaderW
 * 字符串转整数
 * 2019/6/25
 */

public class Solution4 {



    public static int myAtoi(String str) {
        str = str.trim();
        if (str == null || str.length() == 0) {
            return 0;
        }
        int left = 0;
        Character prefix = str.charAt(0);
        int len = str.length();

        int res;

        if (prefix.equals('-') || prefix.equals('+')) {
            left++;
            while (left < len && Character.isDigit(str.charAt(left))) {
                left++;
            }

            if (left == 1) {
                return 0;
            }

        } else if (Character.isDigit(prefix)) {
            while (left < len && Character.isDigit(str.charAt(left))) {
                left++;
            }
            if (left == 0) {
                return 0;
            }
        } else {
            return 0;
        }

        try {
            res = Integer.valueOf(str.substring(0, left));
        } catch (NumberFormatException e) {
            if (prefix.equals('+')) {
                res = Integer.MAX_VALUE;
            } else if (prefix.equals('-')) {
                res = Integer.MIN_VALUE;
            } else {
                res = Integer.MAX_VALUE;
            }
        }


        return res;
    }

    public static void main(String[] args) {
        System.out.println(myAtoi("-91283472332"));
    }
}
