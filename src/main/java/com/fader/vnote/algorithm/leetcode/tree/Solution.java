package com.fader.vnote.algorithm.leetcode.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author FaderW
 * 二叉树相关算法
 */
public class Solution {

    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        if (root == null) {
            return Collections.emptyList();
        }
        TreeNode head = root;
        LinkedList<List<Integer>> res = new LinkedList<>();
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.offer(head);

        while (!queue.isEmpty()) {
            List<Integer> list = new ArrayList<>();
            int queueSize = queue.size();
            for (int i = 0; i < queueSize; i++) {
                TreeNode treeNode = queue.poll();
                if (treeNode.left != null) {
                    queue.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    queue.offer(treeNode.right);
                }
                list.add(treeNode.val);
            }
            res.addFirst(list);

        }
        return res;
    }

    /**
     * 108.将有序数组转换为二叉搜索树
     * @param nums
     * @return
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return findRoot(nums, 0, nums.length-1);
    }

    private TreeNode findRoot(int[] nums, int left, int right) {
        int mid = (left + right) >>> 1;
        TreeNode newNode = new TreeNode(nums[mid]);
        if (left == right) {
            return newNode;
        }
        if (mid > left) {
            newNode.left = findRoot(nums, left, mid-1);
        }
        newNode.right = findRoot(nums, mid+1, right);
        return newNode;
    }

    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (Math.abs(height(root.left) - height(root.right)) > 1) {
            return false;
        }
        if (!isBalanced(root.left)) {
            return false;
        }
        if (!isBalanced(root.right)) {
            return false;
        }

        return true;
    }


    private int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(height(root.left), height(root.right)) + 1;
    }

    /**
     * 112.路径总和
     * @param root
     * @param sum
     * @return
     */
    public boolean hasPathSum(TreeNode root, int sum) {
        if (root == null || sum < 0) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return sum == root.val;
        }
        return hasPathSum(root.left, sum - root.val) || hasPathSum(root.right, sum - root.val);
    }

    /**
     * 113.路径总和
     * @param root
     * @param sum
     * @return
     */
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        if (root == null) {
            return Collections.emptyList();
        }
        List<List<Integer>> res = new ArrayList<>();
        pathSum(root, sum, res, new LinkedList<>());
        return res;
    }

    private void pathSum(TreeNode treeNode, int sum, List<List<Integer>> res, LinkedList<Integer> list) {
        if (treeNode == null) {
            return;
        }
        list.add(treeNode.val);
        if (treeNode.left == null && treeNode.right == null && sum == treeNode.val) {
            res.add(new ArrayList<>(list));
            return;
        }
        pathSum(treeNode.left, sum - treeNode.val, res, list);
        pathSum(treeNode.right, sum - treeNode.val, res, list);
        list.removeLast();
    }

    static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}