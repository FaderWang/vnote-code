package com.fader.vnote.collection.list;

/**
 * @author FaderW
 * 2019/3/29
 */

public class LinkedListCase {



    public Node reserveList(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node prev = null;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;

            prev = current;
            current = next;
        }

        return prev;
    }

    public Node reserveList2(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node next = current.next;
        current.next = null;
        Node reserveNode = reserveList2(next);
        next.next = current;

        return reserveNode;
    }

    public Node reserveList3(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node prev = null;
        Node next;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return null;
    }

    public Node reserveList4(Node current) {
        if (current == null || current.next == null) {
            return current;
        }
        Node next = current.next;
        Node reserve = reserveList4(next);
        next.next = current;

        return reserve;
    }

    /**
     * 插入排序
     * @param node
     * @return
     */
    public Node insertionSortList(Node<Integer> node) {
        if (node == null || node.next == null) {
            return node;
        }
        Node<Integer> newNode = new Node(0);
        Node<Integer> cur = node;
        Node<Integer> prev = newNode;
        while (cur != null) {
            Node next = cur.next;
            while (prev.next != null && prev.next.data < cur.data) {
                prev = prev.next;
            }
            cur.next = prev.next;
            prev.next = cur;

            cur = next;
            prev = newNode;
        }

        return newNode.next;
    }

    /**
     * 插入排序
     * @param node
     * @return
     */
    public Node insertionSortList2(Node<Integer> node) {
        if (node == null || node.next == null) {
            return node;
        }
        Node<Integer> newNode = new Node<>(0);
        Node<Integer> cur = node;
        Node<Integer> prev = newNode;
        Node<Integer> tail = null;
        while (cur != null) {
            Node next = cur.next;
            if (tail == null) {
                prev.next = cur;
                tail = cur;
            } else {
                if (cur.data >= tail.data) {
                    tail.next = cur;
                    tail = cur;
                } else {
                    while (prev.next != null && prev.next.data < cur.data) {
                        prev = prev.next;
                    }
                    cur.next = prev.next;
                    prev.next = cur;
                }
            }

            cur = next;
            prev = newNode;
        }

        return newNode.next;
    }

    public Node mergeSortList(Node<Integer> node) {
        return null;
    }

    public static void main(String[] args) {
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);

        node1.next = node4;
        node4.next = node3;
        node3.next = node2;


        LinkedListCase linkedListCase = new LinkedListCase();
        Node r1 = linkedListCase.insertionSortList2(node1);
        System.out.println(r1.data);
        System.out.println(r1.next.data);
        System.out.println(r1.next.next.data);
        System.out.println(r1.next.next.next.data);
    }

    public static class Node<V> {
        V data;
        Node<V> next;

        public Node(V v) {
            this.data = v;
        }
    }
}
