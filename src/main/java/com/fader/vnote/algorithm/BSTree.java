package com.fader.vnote.algorithm;

/**
 * @author FaderW
 * 二叉查找树
 * 2019/7/22
 */

public class BSTree {

    private Node tree;

    private int size;

    static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    public int size() {
        return this.size;
    }

    public void add(int value) {
        if (tree == null) {
            tree = new Node(value);
            size = 1;
        } else if (value == tree.value) {
            return;
        } else if (value < tree.value) {
            insert(tree, tree.left, value);
        } else if (value > tree.value) {
            insert(tree, tree.right, value);
        }
    }

    private boolean insert(Node p, Node c, int value) {
        if (null == c) {
            c = new Node(value);
            if (p.value > value) {
                p.left = c;
            } else {
                p.right = c;
            }
            size++;
        } else if (c.value == value) {
            return false;
        } else if (value < c.value) {
            insert(c, c.left, value);
        } else {
            insert(c, c.right, value);
        }

        return true;
    }


    /**
     * 删除节点，分为三种情况：
     * 1.删除的节点为叶子节点，父节点直接指向NULL
     * 2.删除的节点只有一个子节点，父节点直接指向该节点的唯一子节点
     * 3.有左右节点，分两步，找到节点右子数最小节点（没有左节点）；替换掉当前要删除节点，
     * 最后就问题就转变成删除该最小节点，与上面两种情况一致
     * @param value
     * @return
     */
    public boolean delete(int value) {
        Node p = tree;
        Node pp = null;
        while (p != null && p.value != value) {
            pp = p;
            if (value < p.value) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        if (p == null) {
            System.out.println("do not find node to delete");
            return false;
        }
        //获取又子数最小节点
        if (p.left != null && p.right != null) {
            Node minp = p.right;
            Node minpp = p;
            while (minp.left != null) {
                minpp = minp;
                minp = minp.left;
            }
            p.value = minp.value;
            p = minp;
            pp = minpp;
        }

        //p的子节点
        Node child;
        //删除节点
        if (p.left != null) {
            child = p.left;
        } else if (p.right != null) {
            child = p.right;
        } else {
            child = null;
        }

        if (pp == null) {
            tree = null;
        } else if (pp.left == p){
            pp.left = child;
        } else {
            pp.right = child;
        }

        size--;
        return true;
    }


    public static void main(String[] args) {
        BSTree bsTree = new BSTree();
        bsTree.add(10);
        bsTree.add(15);
        bsTree.add(25);
        bsTree.add(2);
        bsTree.add(13);

        bsTree.delete(15);
        System.out.println(bsTree);
    }
}
