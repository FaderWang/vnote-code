package com.fader.vnote.algorithm.leetcode;

/**
 * @author FaderW
 * 找出最大回文字符串
 * 2019/6/17
 */

public class Solution3 {


    /**
     * 中心思想，遍历字符串，分别以每个字符串为中心向两边扩散，寻找最大回文数
     * 同时存储当前回文数的右边界，如果在当前右边界之内，找到与之对应的右边界中心点左侧的中心点
     * 根据回文串的特性，以左侧中心点为中心的回文长度等于以当前点为中心点的回文长度。
     * 同时每次计算出当前最长回文长度。
     * 注意有可能偶数情况下中间点为两个字符之前的空白，我们可以在每个字符间插入特殊字符。
     * @param s
     * @return
     */
    public static String longestPalindrome(String s) {
        //预处理，插入间隔字符&
        StringBuilder stringBuilder = new StringBuilder("&");
        for (int i = 0; i < s.length(); i++) {
            stringBuilder.append(s.charAt(i))
                    .append("&");
        }
        s = stringBuilder.toString();
        int[] halfLenArray = new int[s.length()];
        int rightSide = 0;
        //边界中心索引
        int rightSideCenter = 0;

        int center = 0;
        int longestHalf = 0;

        for (int i = 0; i < s.length(); i++) {
            boolean needCalc = true;
            if (i < rightSide) {
                int leftCenter = 2 * rightSideCenter - i;
                halfLenArray[i] = halfLenArray[leftCenter];
            }
            //如果超过边界，则处理为等于边界
            if (halfLenArray[i] + i > rightSide) {
                halfLenArray[i] = rightSide - i;
            } else if (halfLenArray[i] + i > rightSide) {
                needCalc = false;
            }

            if (needCalc) {
                while (i - 1 - halfLenArray[i] >= 0 && i + 1 + halfLenArray[i] < s.length()) {
                    if (s.charAt(i - 1 - halfLenArray[i]) != s.charAt(i + 1 + halfLenArray[i])) {
                        break;
                    }
                    halfLenArray[i]++;
                }
            }

            rightSide = i + halfLenArray[i];
            rightSideCenter = i;

            if (halfLenArray[i] > longestHalf) {
                longestHalf = halfLenArray[i];
                center = i;
            }
        }

        StringBuilder res = new StringBuilder();
        for (int i = center - longestHalf + 1; i < center + longestHalf; i+=2) {
            res.append(s.charAt(i));
        }

        return res.toString();
    }

    public static void main(String[] args) {
        String s = "abacab";
        System.out.println(longestPalindrome(s));
    }
}
