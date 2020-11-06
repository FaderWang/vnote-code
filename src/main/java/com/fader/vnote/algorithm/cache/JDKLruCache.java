package com.fader.vnote.algorithm.cache;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class JDKLruCache<K, V> {

    private Map<K, V> map;

    private int capacity;

    private Node<K, V> head;

    private Node<K, V> tail;


    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;
        Node<K, V> prev;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public JDKLruCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
    }

    public int size() {
        return map.size();
    }

    public void put(K key, V value) {
        V oldValue = map.put(key, value);
        if (oldValue != null) {
            return;
        }
        Node<K, V> newNode = new Node<>(key, value);
        if (head == null) {
            head = tail = newNode;
        } else {
            Node<K, V> t = tail;
            t.next = newNode;
            newNode.prev = t;
            tail = newNode;
        }
        afterNodeInsertion();
    }

    public V get(K key) {
        V value;

        // afterNodeAccess
        if ((value = map.get(key)) != null) {
           Node<K, V> p = head;
           while (p != null) {
               if (p.key == key || p.key.equals(key)) {
                   afterNodeAccess(p);
                   break;
               }
               p = p.next;
           }
        }

        return value;
    }

    public void remove(K key) {
        V value = map.remove(key);
        if (value == null) {
            return;
        }
        // findNode
        Node<K, V> p = head;
        Node<K, V> e = null;
        while (p != null) {

            if (p.key == key || p.key.equals(key)) {
                e = p;
                break;
            }
            p = p.next;
        }
        if (e != null) {
            afterNodeRemove(e);
        }
    }

    private void afterNodeInsertion() {
        Node<K, V> first;
        if (size() > capacity && (first = head) != null) {
            map.remove(first.key);
            afterNodeRemove(first);
        }
    }

    /**
     * 数据访问后将数据移动到末尾
     * @param node
     */
    private void afterNodeAccess(Node<K, V> node) {
        if (node == tail) {
            return;
        }
        if (node == head) {
            head = node.next;
            node.next.prev = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        node.next = null;

        tail.next = node;
        node.prev = tail;
        tail = node;
    }

    private void afterNodeRemove(Node<K, V> e) {
        Node<K, V> n = e.next, p = e.prev;
        e.prev = e.next = null;
        if (p == null) {
            head = n;
        } else {
            p.next = n;
        }
        if (n == null) {
            tail = p;
        } else {
            n.prev = p;
        }
    }

    public static void main(String[] args) {
        JDKLruCache<String, String> lruCache = new JDKLruCache<>(5);
        IntStream.range(0, 5).forEach(i -> {
            lruCache.put(i + "", i + "");
        });

        lruCache.get("1");
        lruCache.get("3");

        IntStream.range(5, 8).forEach(i -> {
            lruCache.put(i + "", i + "");
        });

        lruCache.size();
        System.out.println(lruCache);
    }
}
