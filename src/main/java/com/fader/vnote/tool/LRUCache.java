package com.fader.vnote.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 * 2019/4/19
 * LRU 即Least Recently Used，遵循最近最少使用策略
 */
public class LRUCache<K, V> {

    /**
     * current element count
     */
    private int currentSize = 0;

    /**
     * largest capacity;
     */
    private int capacity;

    /**
     * lru store data map
     */
    private final Map<K, V> map;

    /**
     * head node
     */
    public transient Node<K, V> head;

    /**
     * tail node
     */
    private transient Node<K, V> tail;


    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = tail = null;
    }

    public void put(K k, V v) {
        if (null == k) {
            throw new NullPointerException("parameter k can not be null");
        }
        map.put(k, v);
        Node<K, V> current = new Node<>(k, v);

        Node<K, V> last;


        if ((last = tail) == null) {
            // tail为空说明链表为空，头结点指向当前节点
            head = current;
        } else {
            // 插入尾节点
            last.next = current;
            current.prev = last;
        }
        // 尾节点指向当前节点
        tail = current;

        if (currentSize++ > capacity) {
            // 执行失效策略，去除头结点
            removeHead();
        }
    }

    /**
     * delete tail node
     */
    final void removeHead() {
        Node<K, V> h = head, a = head.next;

        if (null != h) {
            map.remove(h.k);
            head = a;
            a.prev = null;
        }
    }

    public V get(K k) {
        V v = map.get(k);
        if (v != null) {
            Node<K, V> current = head;
            while (true) {
                if (current == null) {
                    break;
                }
                // 当前节点插入尾部
                if (current.k == k) {
                    Node<K, V> p = current.prev;
                    Node<K, V> a = current.next;
                    Node<K, V> last = tail;

                    if (p == null) {
                        // 如果前置节点为空,那么头指针将指向后置节点
                        head = a;
                    } else {
                        // 不为空
                        p.next = a;
                    }
                    if (a != null) {
                        // 如果后置节点不为空，prev将指向前置节点
                        a.prev = p;
                    } else {
                        // 如果为空，last指针将指向前置节点
                        last = p;
                    }
                    // 如果last为空，则代表当前只有一个元素，head指向current
                    if (last == null) {
                        head = current;
                    } else {
                        //不为空 则链接两个节点
                        current.prev = last;
                        last.next = current;
                    }
                    // 尾节点指向current
                    tail = current;
                    break;
                }
                current = current.next;
            }
        }

        return v;
    }


    static class Node<K, V> {
        K k;
        V v;

        Node<K, V> next;
        Node<K, V> prev;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }


    public static void main(String[] args) {
        LRUCache<String, String> cache = new LRUCache<>(4);
        cache.put("name", "wangyuxin");
        cache.put("age", "20");
        cache.put("sex", "female");
        cache.put("birth", "1995");

        System.out.println(cache.get("name"));
        cache.put("nick", "dog");

        Node<String, String> p = cache.tail;
        System.out.println((p.k));
        System.out.println((p = p.prev).k);
        System.out.println((p = p.prev).k);
        System.out.println((p = p.prev).k);
    }
}
