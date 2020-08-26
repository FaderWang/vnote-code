package com.fader.vnote.algorithm.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 * LFU最少使用次数缓存，根据使用频率淘汰数据
 * @param <K>
 * @param <V>
 */
public class LFUCache<K, V> {

    // 当前元素个数
    private int size;
    // 容量
    private int capacity;

    private Map<K, Node> map;

    private Node<K, V> head = new Node<>();

    private Node<K, V> tail = new Node<>();

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>(capacity);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K k) {
        V value = null;
        Node<K, V> node = map.get(k);
        if (node != null) {
            value = node.value;
            freqPlus(node);
        }

        return value;
    }

    public void put(K k, V value) {
        Node node = map.get(k);
        if (node != null) {
            node.value = value;
            freqPlus(node);
        } else {
            eliminate();
            node = new Node(k, value);
            map.put(k, node);
            size++;

            // 放入尾节点
            Node last = tail.prev;
            last.next = node;
            node.prev = last;
            node.next = tail;
            tail.prev = node;

            freqPlus(node);
        }
    }

    /**
     * 淘汰数据
     */
    private void eliminate() {
        if (size < capacity) {
            return;
        }
        Node last = tail.prev;
        tail.prev = last.prev;
        last.prev.next = tail;
        map.remove(last.key);
        size--;

        // help gc
        last = null;
    }

    private void freqPlus(Node<K, V> node) {
        node.frequency++;
        // 调整位置
        Node prev = node.prev;
        while (prev != null) {
            if (prev.frequency > node.frequency || prev == head) {
                // 将当前节点断开,前置节点next指向后置节点，后置节点prev指向前置节点
                node.prev.next = node.next;
                node.next.prev = node.prev;

                // 当前节点放到合适的位置
                Node prevNext = prev.next;
                prev.next = node;
                node.prev = prev;
                node.next = prevNext;
                prevNext.prev = node;
                break;
            }
            prev = prev.prev;
        }
    }

    static class Node<K, V> {
        K key;
        V value;
        int frequency;
        Node<K, V> prev;
        Node<K, V> next;

        Node(K k, V v) {
            this.key = k;
            this.value = v;
        }

        Node() {

        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Node cur = head.next;
        while (cur != null && cur != tail) {
            stringBuilder.append(cur.key).append("=").append(cur.value);
            if (cur.next != tail) {
                stringBuilder.append(",");
            }
            cur = cur.next;
        }

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        LFUCache<String, String> cache = new LFUCache<>(4);
        cache.put("name", "wangyuxin");
        cache.put("age", "24");
        cache.put("sex", "male");
        cache.put("birth", "1995");

        cache.get("name");
        cache.get("birth");
        cache.get("name");

        cache.put("hostry", "ball");


        System.out.print(cache);
    }
}
