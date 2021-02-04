package com.fader.vnote.algorithm.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 * @Date 2021/2/4 9:58
 */
public class LFUCache2 {

    private int capacity;

    private DLinkedNode head;

    private DLinkedNode tail;

    public Map<Integer, DLinkedNode> cacheMap = new HashMap<>();

    class DLinkedNode {
        int key;
        int value;
        int frequent;
        DLinkedNode next;
        DLinkedNode prev;

        public DLinkedNode(){};

        public DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    public LFUCache2(int capacity) {
        this.capacity = capacity;
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }

    public void put(int key, int value) {
        DLinkedNode node = cacheMap.get(key);
        if (node != null) {
            node.value = value;
            plusFreq(node);
        } else {
            if (capacity <= 0) {
                return;
            }
            if (cacheMap.size() == capacity) {
                DLinkedNode remove = removeTail();
                cacheMap.remove(remove.key);
            }
            node = new DLinkedNode(key, value);
            cacheMap.put(key, node);
            enqueue(node);
            plusFreq(node);
        }
    }

    public int get(int key) {
        DLinkedNode node = cacheMap.get(key);
        if (node != null) {
            plusFreq(node);
        }
        return node == null ? -1 : node.value;
    }

    /**
     * 删除尾节点
     * @return
     */
    private DLinkedNode removeTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);

        return res;
    }

    private void removeNode(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void enqueue(DLinkedNode node) {
        node.next = tail;
        node.prev = tail.prev;
        tail.prev.next = node;
        tail.prev = node;

    }

    /**
     * increment frequent count
     * @param node
     */
    private void plusFreq(DLinkedNode node) {
        node.frequent++;
        DLinkedNode prev = node.prev;
        while (prev != null) {
            if (prev.frequent > node.frequent || prev == head) {
                // 将当前节点断开,前置节点next指向后置节点，后置节点prev指向前置节点
                node.prev.next = node.next;
                node.next.prev = node.prev;

                // 当前节点放到合适的位置
                DLinkedNode prevNext = prev.next;
                prev.next = node;
                node.prev = prev;
                node.next = prevNext;
                prevNext.prev = node;
                break;
            }
            prev = prev.prev;
        }
    }

    public boolean contains(int key) {
        return cacheMap.containsKey(key);
    }

    public static void main(String[] args) {
        LFUCache2 cache = new LFUCache2(4);
        for (int i = 1; i < 5; i++) {
            cache.put(i, i);
        }

        cache.get(1);
        for (int i = 0; i < 4; i++) {
            cache.get(i);
        }
        cache.put(5, 5);

        System.out.println(cache.contains(1));
        System.out.println(cache.contains(2));
        System.out.println(cache.contains(3));
        System.out.println(cache.contains(4));
        System.out.println(cache.contains(5));
    }
}
