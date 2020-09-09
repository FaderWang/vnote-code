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
     * 链表反转
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        // 遍历链表过程中改变指针方向，将当前节点next指向prev，需要一个变量prev保存上一个节点
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = head;
        ListNode prev = null;
        while (p != null) {
            ListNode next = p.next;
            p.next = prev;
            prev = p;
            p = next;
        }

        return prev;
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

    /**
     * 61.给定一个链表，旋转链表，将链表每个节点向右移动 k 个位置，其中 k 是非负数。
     * 输入: 1->2->3->4->5->NULL, k = 2
     * 输出: 4->5->1->2->3->NULL
     * 解释:
     * 向右旋转 1 步: 5->1->2->3->4->NULL
     * 向右旋转 2 步: 4->5->1->2->3->NULL
     * @param head
     * @param k
     * @return
     */
    public ListNode rotateRight(ListNode head, int k) {
        /**
         * 移动链表长度将会恢复原链表，k = k%size
         */
        if (head == null || head.next == null) {
            return head;
        }
        int size = 0;
        ListNode cur = head;
        while (cur != null) {
            size++;
            cur = cur.next;
        }
        k = k%size;
        return rotateRightHelper(head, k);
    }

    public ListNode rotateRightHelper(ListNode head, int k) {
        if (k == 0) {
            return head;
        }
        ListNode cur = head;
        ListNode prev = null;
        while (cur.next != null) {
            prev = cur;
            cur = cur.next;
        }
        prev.next = null;
        cur.next = head;
        return rotateRightHelper(cur, k-1);
    }

    /**
     * 445.两数相加
     * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
     * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
     *
     * 进阶：
     * 如果输入链表不能修改该如何处理？换句话说，你不能对列表中的节点进行翻转。
     *
     * 输入：(7 -> 2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 8 -> 0 -> 7
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode p1 = l1;
        ListNode p2 = l2;
        int value1 = 0;
        while (p1 != null) {
            value1 = value1 * 10 + p1.val;
            p1 = p1.next;
        }
        int value2 = 0;
        while (p2 != null) {
            value2 = value2 * 10 + p2.val;
            p2 = p2.next;
        }

        int value = value1 + value2;
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        char[] chars = Integer.valueOf(value).toString().toCharArray();
        for (char c : chars) {
            cur.next = new ListNode(Integer.valueOf(String.valueOf(c)));
            cur = cur.next;
        }

        return dummy.next;
    }

    public static void main(String[] args) {
        char[] chars = String.valueOf(7807).toCharArray();
        for (char c : chars) {
            System.out.print(Integer.valueOf(String.valueOf(c)));
        }
    }
}
