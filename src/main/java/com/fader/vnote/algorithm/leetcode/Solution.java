package com.fader.vnote.algorithm.leetcode;

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

    public void replace(String s) {
        s = "yes";
    }

    public int waysToChange(int n) {
        int[] coins = new int[]{1, 5, 10, 25};
        int[] dp = new int[n+1];
        for (int i = 0; i < coins.length; i++) {
            if (n - coins[i]  < 0) {
                continue;
            }
            dp[n] = dp[n] + dp[n - coins[i]];
        }

        return dp[n];

//        int[] f = new int[n + 1];
//        f[0] = 1;
//        for (int c = 0; c < 4; ++c) {
//            int coin = coins[c];
//            for (int i = coin; i <= n; ++i) {
//                f[i] = (f[i] + f[i - coin]) % MOD;
//            }
//        }
//        return f[n];n

    }





    public static void main(String[] args) {
//        System.out.println(lengthOfLongestSubstring("abba"));
//        String s = "abcabcabc";
//        System.out.println(s.indexOf('a', 4));
        Solution solution = new Solution();
//        String str = new String("no");
//        solution.replace(str);
//        System.out.println(str);

        System.out.println(solution.waysToChange(1));
    }
}