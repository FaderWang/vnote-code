package com.fader.vnote.algorithm.leetcode.tree;

import java.util.*;

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
     * 二叉树层序遍历
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return Collections.EMPTY_LIST;
        }
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int queueSize = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < queueSize; i++) {
                TreeNode treeNode = queue.poll();
                list.add(treeNode.val);
                if (treeNode.left != null) {
                    queue.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    queue.offer(treeNode.right);
                }
            }
            res.add(list);

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

    int max_sum = Integer.MIN_VALUE;

    /**
     * 124.二叉树中最大路径和
     * @param root
     * @return
     */
    public int maxPathSum(TreeNode root) {
        maxGen(root);
        return max_sum;
    }

    /**
     * 定义该节点的最大贡献值，为以该节点为根节点经过该节点的最大路径和。
     * @param root
     * @return
     */
    private int maxGen(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 计算左右子树的最大贡献值，如果最大贡献值为负数，则舍弃该节点。即值取0。
        int left = Math.max(0, maxGen(root.left));
        int right = Math.max(0, maxGen(root.right));
        // 更新最大路径和，为左子树+右子树+当前值与当前最大路径和比较。
        max_sum = Math.max(max_sum, left + right + root.val);

        return root.val + Math.max(left, right);

    }

    /**
     * 94.二叉树中序遍历
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
//        inOrder(root, res);
//
//        return res;
        // 2.非递归法，模拟出栈入栈过程
        Deque<TreeNode> stack = new ArrayDeque<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            res.add(root.val);
            root = root.right;
        }

        return res;
    }


    private void inOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        inOrder(root.left, res);
        res.add(root.val);
        inOrder(root.right, res);
    }

    /**
     * 二叉树前序遍历
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
//        preOrder(root, res);
//        return res;
        Deque<TreeNode> stack = new ArrayDeque<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                res.add(root.val);
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            root = root.right;
        }

        return res;
    }

    private void preOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preOrder(root.left, res);
        preOrder(root.right, res);
    }

    /**
     * 二叉树后续遍历
     * @param root
     * @return
     */
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
//        postOrder(root, res);
        Deque<TreeNode> stack = new ArrayDeque<>();
        // prev节点记录上一次访问的节点
        TreeNode prev = null;
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }

            root = stack.pop();
            // 如果右节点已经访问过了，则可以访问该节点。
            if (root.right == null || root.right == prev) {
                res.add(root.val);
                prev = root;
                root = null;
            } else {
                stack.push(root);
                root = root.right;
            }
        }

        return res;
    }

    private void postOrder(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        postOrder(root.left, res);
        postOrder(root.right, res);
        res.add(root.val);
    }



    static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}