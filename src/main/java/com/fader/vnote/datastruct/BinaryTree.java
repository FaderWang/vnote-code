package com.fader.vnote.datastruct;

import java.util.LinkedList;

/**
 * @author FaderW
 * 2020/6/23
 */

public class BinaryTree {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public static void preOrderTraveral(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        System.out.println(treeNode.val);
        preOrderTraveral(treeNode.left);
        preOrderTraveral(treeNode.right);
    }

    public static void inOrderTraveral(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        inOrderTraveral(treeNode.left);
        System.out.print(treeNode.val + "\t");
        inOrderTraveral(treeNode.right);
    }

    public static void inOrderTraveralWithStack(TreeNode treeNode) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                System.out.print(treeNode.val + "\t");
                treeNode = treeNode.right;
            }
        }
    }

    /**
     * 前序遍历非递归
     * @param treeNode
     */
    public static void preOrderTraveralWithStack(TreeNode treeNode) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                System.out.print(treeNode.val + "\t");
                treeNode = treeNode.left;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.right;
            }
        }
    }

    public static void postOrderTraveral(TreeNode treeNode) {
        if (treeNode == null) {
            return;
        }
        postOrderTraveral(treeNode.left);
        postOrderTraveral(treeNode.right);
        System.out.println(treeNode.val);
    }

    public static void postOrderTraveralWithStack(TreeNode treeNode) {
        LinkedList<TreeNode> stack = new LinkedList<>();
        TreeNode last = null;
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            treeNode = stack.peek();
            if (treeNode.right == null || treeNode.right == last) {
                System.out.println(treeNode.val);
                stack.pop();
                last = treeNode;
                treeNode = null;
            } else {
                treeNode = treeNode.right;
            }
        }
    }



    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(1);
        treeNode.left = new TreeNode(2);
        treeNode.right = new TreeNode(3);
        treeNode.left.left = new TreeNode(4);
        treeNode.left.right = new TreeNode(5);

//        preOrderTraveral(treeNode);
//        preOrderTraveralWithStack(treeNode);
//        inOrderTraveral(treeNode);
//        inOrderTraveralWithStack(treeNode);
//        postOrderTraveral(treeNode);
        postOrderTraveralWithStack(treeNode);
    }
}
