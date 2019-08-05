package com.fader.vnote.tool;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FaderW
 * 2019/6/3
 */

public class LinkedHashMapCache<K, V> {

    /**
     * capacity for cache
     */
    private int capacity;

    /**
     * real container for cache
     */
    private LinkedHashMap<K, V> linkedHashMap;


    public LinkedHashMapCache(int capacity) {
        this.capacity = capacity;
        linkedHashMap = new CacheMap<>();

    }

    class CacheMap<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 8590816782678643129L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            if (size() > capacity) {
                return true;
            }
            return false;
        }
    }

    public void put(K k, V v) {
        linkedHashMap.put(k, v);
    }


    public V get(K key) {
        return linkedHashMap.get(key);
    }
}
