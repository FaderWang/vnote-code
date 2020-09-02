package com.fader.vnote.datastructure.tree;

/**
 * @author FaderW
 * BST二叉搜索树
 */
public class BinarySearchTree {

    private Node root;

    public Node find(int val) {
        Node p = root;
        while (p != null) {
            if (p.val == val) {
                return p;
            } else if (val < p.val) {
                p = p.left;
            } else if (val > p.val) {
                p = p.right;
            }
        }

        return null;
    }

    /**
     * 插入跟查找过程类似，从根节点开始
     * @param val
     * @return
     */
    public void insert(int val) {
        if (root == null) {
            root = new Node(val);
            return;
        }
        Node p = root;
        while (p != null) {
            if (val < p.val) {
                if (p.left == null) {
                    p.left = new Node(val);
                    return;
                } else {
                    p = p.left;
                }
            } else {
                if (p.right == null) {
                    p.right = new Node(val);
                    return;
                } else {
                    p = p.right;
                }
            }
        }
    }

    public void delete(int val) {
        Node p = root;
        Node pp = null;
        while (p != null) {
            if (val < p.val) {
                pp = p;
                p = p.left;
            } else if (val > p.val) {
                pp = p;
                p = p.right;
            } else {
                break;
            }
        }

        if (p == null) {
            return;
        }

        // 要删除的节点有左右两个节点
        if (p.left != null && p.right != null) {
            Node q = p.right;
            Node qq = p;
            while (q.left != null) {
                qq = q;
                q = q.left;
            }
            // swap
            p.val = q.val;
            p = q;
            pp = qq;
        }

        // 如果是叶子节点或者只有一个子节点
        Node child;
        if (p.left != null) {
            child = p.left;
        } else if (p.right != null) {
            child = p.right;
        } else {
            child = null;
        }

        if (pp == null) {
            root = child;
        } else if (pp.left == p) {
            pp.left = child;
        } else {
            pp.right = child;
        }
    }

    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }
}
