package com.fader.vnote.algorithm;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author FaderW
 * 2020/3/23
 */

public class TreeMain {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * 判断二叉树是否对称
     * @param root
     * @return
     */
    public boolean isSymmetric(TreeNode root) {
//        return isSymmetric(root, root);
        if (root == null) {
            return true;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root.left);
        queue.add(root.right);
        while (!queue.isEmpty()) {
            TreeNode t1 = queue.poll();
            TreeNode t2 = queue.poll();
            if (t1 == null && t2 == null) {
                continue;
            }
            if (t1 == null || t2 == null) {
                return false;
            }
            if (t1.val != t2.val) {
                return false;
            }
            queue.add(t1.left);
            queue.add(t2.right);
            queue.add(t1.right);
            queue.add(t2.left);
        }

        return true;
    }


    private boolean isSymmetric(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 != null || node2 != null) {
            return false;
        }
        if (node1.val == node2.val) {
            return isSymmetric(node1.left, node2.right) &&
                    isSymmetric(node1.right, node2.left);
        }

        return false;
    }
}
