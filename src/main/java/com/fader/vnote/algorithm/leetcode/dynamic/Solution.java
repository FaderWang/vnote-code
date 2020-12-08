package com.fader.vnote.algorithm.leetcode.dynamic;

/**
 * @author FaderW
 * 2020/11/8
 */

public class Solution {

    /**
     * 硬币。给定数量不限的硬币，币值为25分、10分、5分和1分，
     * 编写代码计算n分有几种表示法。(结果可能会很大，你需要将结果模上1000000007)
     * @param n
     * @return
     */
    public int waysToChange(int n) {
        int dp[];

        return -1;
    }

    public int helper(int n) {
        if (n < 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return helper(n-1) + helper(n-10) + helper(n-5) + helper(n-25);
    }
}
