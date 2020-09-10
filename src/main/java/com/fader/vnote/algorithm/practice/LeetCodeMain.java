package com.fader.vnote.algorithm.practice;


import jdk.nashorn.internal.ir.IfNode;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author FaderW
 * 2019/11/14
 */

public class LeetCodeMain {

    /**
     * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
     * @param text
     * @param pattern
     * @return
     */
    public static boolean isMatch(String text, String pattern) {
        if (pattern.isEmpty()) {
            return text.isEmpty();
        }
        boolean first_match = (!text.isEmpty() && (pattern.charAt(0) == text.charAt(0) || pattern.charAt(0) == '.'));
        if (pattern.length() >= 2 && pattern.charAt(1) == '*') {
            return isMatch(text, pattern.substring(2)) ||
                    (first_match && isMatch(text.substring(1), pattern));
        } else {
            return first_match && isMatch(text.substring(1), pattern.substring(1));
        }

    }

    public static int game(int[] guess, int[] answer) {
        return (guess[0] == answer[0] ? 1 : 0) + (guess[1] == answer[1] ? 1 : 0) +
                (guess[2] == answer[2] ? 1 : 0);
    }

//    public static void main(String[] args) {
////        System.out.println(isMatch("abcd", ".*cd"));
//        ListNode head = new ListNode(4);
//        ListNode sec = new ListNode(1);
//        ListNode third = new ListNode(3);
//        ListNode four = new ListNode(2);
//        head.next = sec;
//        sec.next = third;
//        third.next = four;
//
//        ListNode res = sortList2(head);
//        System.out.println(res.val);
//        System.out.println(res.next.val);
//        System.out.println(res.next.next.val);
//        System.out.println(res.next.next.next.val);
//
//    }

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public static ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // 找到链表的中心点
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode temp = slow.next;
        slow.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(temp);

        ListNode h = new ListNode(0);
        ListNode res = h;
        while (left != null && right != null) {
            if (left.val < right.val) {
                h.next = left;
                left = left.next;
            } else {
                h.next = right;
                right = right.next;
            }
            h = h.next;
        }
        h.next = left != null ? left : right;
        return res.next;
    }

    public static ListNode sortList2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // 链表查找长度需要遍历
        int length = 0;
        ListNode h = head;
        while (h != null) {
            h = h.next;
            length++;
        }
        // 步长初始为1
        ListNode res = new ListNode(0);
        res.next = head;
        int step = 1;
        while (step < length) {
            h = res.next;
            ListNode pre = res;
            while (h != null) {
                // get the two merge head `h1`, `h2`
                int i = step;
                ListNode h1 = h;
                while (h != null && i > 0) {
                    h = h.next;
                    i--;
                }
                // h2 is none
                if (i > 0) {
                    break;
                }
                i = step;
                ListNode h2 = h;
                while (h != null && i > 0) {
                    h = h.next;
                    i--;
                }
                int c1 = step;
                int c2 = step - i;
                // merge c1 c2
                while (c1 > 0 && c2 > 0) {
                    if (h1.val < h2.val) {
                        pre.next = h1;
                        h1 = h1.next;
                        c1--;
                    } else {
                        pre.next = h2;
                        h2 = h2.next;
                        c2--;
                    }
                    pre = pre.next;
                }
                pre.next = c1 > 0 ? h1 : h2;
                while (c1 > 0 || c2 > 0) {
                    pre = pre.next;
                    c1--;
                    c2--;
                }
                pre.next = h;
            }
//            r.next = h;
            step*=2;
        }
        return res.next;
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
//        if (l1 != null && l2 == null) {
//            return l1;
//        } else if (l1 == null && l2 != null) {
//            return l2;
//        } else if (l1 == null && l2 == null) {
//            return null;
//        }
//        ListNode p1 = l1;
//        ListNode p2 = l2;
//        ListNode res = new ListNode(0);
//        ListNode p = res;
//        while (p1 != null && p2 != null) {
//            if (p1.val <= p2.val) {
//                p.next = p1;
//                p1 = p1.next
//            } else {
//                p.next = p2;
//                p2 = p2.next;
//            }
//            p = p.next;
//        }
//        if (p1 != null) {
//            p.next = p1;
//        }
//        if (p2 != null) {
//            p.next = p2;
//        }
//        return res.next;

        /**
         * 递归
         */
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val <= l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    public ListNode deleteDuplicates(ListNode head) {
//        if (head == null || head.next == null) {
//            return head;
//        }
//        ListNode cur = head;
//        ListNode next;
//        while (cur != null && cur.next != null) {
//            next = cur.next;
//            if (cur.val == next.val) {
//                cur.next = next.next;
//                next.next = null;
//            } else {
//                cur = cur.next;
//            }
//        }
//        return head;

        if (head == null || head.next == null) {
            return head;
        }
        if (head.val == head.next.val) {
            head.next = deleteDuplicates(head.next.next);
        } else {
            head.next = deleteDuplicates(head.next);
        }
        return head;
    }

    /**
     * 11 盛水最多的容器
     * @param height
     * @return
     */
    public static int maxArea(int[] height) {
        int l = 0;
        int h = height.length - 1;
        int max = 0;
        while (l < h) {
            max = Math.max(max, Math.min(height[l], height[h]) * (h-l));
            if (height[l] < height[h]) {
                l++;
            } else {
                h--;
            }
        }

        return max;
    }


    /**
     * 除数博弈
     * 解题思路：找出小于N的所有解，基于前面的解推导后面的。
     * 如果I的约数里存在直接false的，则为True,如果没有，则为false
     * @param N
     * @return
     */
    public boolean divisorGame(int N) {
        if (N == 1) {
            return false;
        }
        if (N == 2) {
            return true;
        }
        boolean[] dp = new boolean[N+1];
        dp[1] = false;
        dp[2] = true;
        for (int i = 3; i <= N; i++) {
            dp[i] = false;
            for (int j = 1; j < i; j++) {
                if (i%j == 0 && !dp[j-1]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[N];
    }

    private boolean help(int N, int cur) {
        if (N == 1) {
            return cur%2 != 0;
        }
        for (int i = 1; i < N; i++) {
            if (N%i == 0) {
                if (help(N-i, cur+1)) {
                    return true;
                }
            }
        }

        return false;
    }

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        int[] dp = new int[n+1];
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i-1] +  dp[i-2];
        }
        return dp[n];
    }

    public int rob(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        if (nums.length == 3) {
            return Math.max(nums[1], nums[0] + nums[2]);
        }
        int[] dp = new int[nums.length + 1];
        dp[1] = nums[0];
        dp[2] = Math.max(nums[0], nums[1]);
        dp[3] = Math.max(nums[1], nums[0] + nums[2]);
        for (int i = 4; i <= nums.length; i++) {
            int cur = nums[i-1];
            int temp = Math.max(dp[i-3]+cur, dp[i-2]+cur);
            temp = Math.max(temp, dp[i-1]);
            dp[i] = temp;
        }

        return dp[nums.length];
    }

    private int rob2(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        if (nums.length == 3) {
            return Math.max(Math.max(nums[0], nums[1]), nums[2]);
        }

        int[] nums1 = new int[nums.length-3];
        int[] nums2 = new int[nums.length-1];
        System.arraycopy(nums, 2, nums1, 0, nums1.length);
        System.arraycopy(nums, 1, nums2, 0, nums2.length);

        return Math.max(rob3(nums1) + nums[0], rob3(nums2));
    }

    public int maxProfit(int[] prices) {
        int minBuy = prices[0];
        int max = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] - minBuy > max) {
                max = prices[i] - minBuy;
            } else if (prices[i] < minBuy) {
                minBuy = prices[i];
            }
        }

        return max;

    }

    public int maxProfit2(int[] prices) {
        if (prices.length <= 1) {
            return 0;
        }
        int dp[] = new int[prices.length+1];
        int min = prices[0];
        dp[1] = 0;
        for (int i = 2; i <= prices.length; i++) {
            min = Math.min(prices[i-1], min);
            if (prices[i-1] - min > dp[i-1]) {
                dp[i] = prices[i-1] - min;
            } else {
                dp[i] = dp[i-1];
            }
        }
        return dp[prices.length];
    }

    private int numCount = 0;

    public int numberOfSteps (int num) {
//        numberOfSteps2(num);
//
//        return numCount;
        // 使用二进制
        String s = Integer.toBinaryString(num);
        int i = s.length()-1;
        int j = s.replace("0", "").length();

        return i+j;
    }

    public int minCostClimbingStairs(int[] cost) {
        if (cost.length == 2) {
            return Math.min(cost[0], cost[1]);
        }
        if (cost.length == 3) {
            return Math.min(cost[1], cost[0] + cost[2]);
        }
        int dp[] = new int[cost.length+1];
        dp[2] = Math.min(cost[0], cost[1]);
        dp[3] = Math.min(cost[1], cost[0] + cost[2]);
        for (int i = 4; i <= cost.length; i++) {
            dp[i] = Math.min(dp[i-1], dp[i-2] + cost[i-1]);
        }
        return dp[cost.length];
    }

    private void numberOfSteps2(int num) {
        if (num == 0) {
            return ;
        }
        numCount++;
        if (num%2 == 1) {
            numberOfSteps2(num-1);
        } else {
            numberOfSteps2(num/2);
        }

    }

    private int rob3(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        if (nums.length == 2) {
            return Math.max(nums[0], nums[1]);
        }
        int[] dp = new int[nums.length+1];
        dp[1] = nums[0];
        dp[2] = Math.max(nums[0], nums[1]);
        for (int i = 3; i <= nums.length; i++) {
            int cur = nums[i-1];
            int temp = Math.max(dp[i-1], dp[i-2] + cur);
            dp[i] = temp;
        }

        return dp[nums.length];
    }

    private int max = 0;

    public static void main(String[] args) {
        LeetCodeMain main = new LeetCodeMain();
////        System.out.println(main.divisorGame(5));
//        System.out.println(main.numberOfSteps(14));
//        System.out.println(Integer.toBinaryString(14));
        int[] nums = IntStream.of(1,2).toArray();
        System.out.println(main.maxProfit2(nums));
    }

//    public int maxProfit2(int[] prices) {
//        if (prices.length <= 1) {
//            return 0;
//        }
//        int max = 0;
//        for (int i = 1; i < prices.length; i++) {
//            if (prices[i] > prices[i-1]) {
//                max += (prices[i] - prices[i-1]);
//            }
//        }
//
//        return max;
//    }

    /**
     * 两两交换相邻的节点
     * @param head
     * @return
     */
    public static ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode node = swapPairs(head.next.next);
        ListNode cur = head;
        ListNode next = head.next;
        next.next = cur;
        cur.next = node;
        return next;
    }


}
