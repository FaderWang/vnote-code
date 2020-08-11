package com.fader.vnote.algorithm.leetcode.linkedlist;

/**
 * @author FaderW
 */
public class Solution {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    /**
     * 2.两数相加
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode p1 = l1;
        ListNode p2 = l2;
        ListNode res = new ListNode(-1);
        int flag = 0;
        while (p1 != null && p2 != null) {
            int val = flag + p1.val + p2.val;
            if (val >= 10) {
                val = val % 10;
                flag = 1;
            } else {
                flag = 0;
            }
            res.next = new ListNode(val);
        }


        return null;
    }
}
