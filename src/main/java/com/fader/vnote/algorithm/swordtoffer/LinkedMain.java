package com.fader.vnote.algorithm.swordtoffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author FaderW
 */
public class LinkedMain {


    public static int[] reversePrint(ListNode head) {
//        List<Integer> list = new ArrayList<>();
//        reverse(head, list);
//
//        int[] res = new int[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            res[i] = list.get(i);
//        }
//
//        return res;

        // 使用栈节省内存使用量
        LinkedList<Integer> stack = new LinkedList<>();
        while (head != null) {
            stack.push(head.val);
            head = head.next;
        }

        int size = stack.size();
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = stack.pop();
        }

        return res;
    }

    private static void reverse(ListNode head, List<Integer> list) {
        if (head == null) {
            return;
        }
        reverse(head.next, list);
        list.add(head.val);
    }





    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) {
            this.val = val;
        }
    }
}
