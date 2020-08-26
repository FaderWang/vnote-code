package com.fader.vnote.algorithm.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FaderW
 * 基于LRU算法的缓存，缓存清理策略：最近最少使用
 * 2019/6/3
 */

public class LRUCache<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    /**
     * capacity for cache
     */
    private final int capacity;

    /**
     * real container for cache
     */
    private final LinkedHashMap<K, V> linkedHashMap;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        linkedHashMap = new CacheMap<>();
    }

    class CacheMap<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 8590816782678643129L;

        public CacheMap() {
            super(16, 0.75f, true);
        }

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
