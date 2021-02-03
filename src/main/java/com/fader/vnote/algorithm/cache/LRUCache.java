package com.fader.vnote.algorithm.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author FaderW
 * @Date 2021/2/3 17:03
 */
public class LRUCache<K, V> {

    private Map<K, DLinkedNode<K, V>> cache = new HashMap<>();
    private int capacity;
    private DLinkedNode<K, V> head;
    private DLinkedNode<K, V> tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // 头尾节点为伪节点
        this.head = new DLinkedNode<>();
        this.tail = new DLinkedNode<>();
        head.next = tail;
        tail.prev = head;
    }

    class DLinkedNode<K, V> {
        K key;
        V value;
        DLinkedNode<K, V> prev;
        DLinkedNode<K, V> next;

        public DLinkedNode() {

        }

        public DLinkedNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public void put(K key, V value) {
        DLinkedNode<K, V> node = cache.get(key);
        if (node != null) {
            // 如果key存在，先更新value，在移到头部
            node.value = value;
            moveToHead(node);
        } else {
            node = new DLinkedNode<>(key, value);
            cache.put(key, node);
            addToHead(node);
            if (cache.size() > capacity) {
                // 超出容量，移除末尾元素
                DLinkedNode<K, V> remove = removeTail();
                cache.remove(remove.key);
            }
        }
    }

    public V get(K key) {
        DLinkedNode<K, V> node = cache.get(key);
        if (node != null) {
            moveToHead(node);
        }

        return node == null ? null : node.value;
    }

    private DLinkedNode<K, V> removeTail() {
        DLinkedNode<K, V> res = tail.prev;
        removeNode(res);
        return res;
    }

    private void removeNode(DLinkedNode<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void moveToHead(DLinkedNode<K,V> node) {
        // 如果当前正好时首元素，不需要移动
        if (head.next == node) {
            return;
        }
        // 将当前元素先删除，再移动到首部
        removeNode(node);
        addToHead(node);
    }

    private void addToHead(DLinkedNode<K,V> node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    public boolean contains(K key) {
        return cache.containsKey(key);
    }

    public Set<K> keySet() {
        return cache.keySet();
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> lruCache = new LRUCache<>(4);

        for (int i = 1; i <= 4; i++) {
            lruCache.put(i, i + "");
        }
        printKeySet(lruCache.keySet());
        lruCache.put(5, "5");
        printKeySet(lruCache.keySet());
        lruCache.put(6, "6");
        printKeySet(lruCache.keySet());
        lruCache.get(3);
        lruCache.get(4);
        lruCache.put(7, "7");
        printKeySet(lruCache.keySet());
        lruCache.put(3, "3");
        printKeySet(lruCache.keySet());
        lruCache.put(8, "8");
        printKeySet(lruCache.keySet());
    }

    public static void printKeySet(Set set) {
        set.stream().forEach(k -> System.out.print(k + ", "));
        System.out.println();
    }
}
