package com.fader.vnote.collection.map;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author FaderW
 * 2019/7/25
 */

public class MapSample {

    public static void hashMapTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 10);
        map.entrySet();

    }

    public static void linkedHashMapTest() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("age", "20");
    }

    public static void hashTableTest() {
        Map<String, Object> map = new Hashtable<>();
        System.out.println(map.size());
        map.put("name", "hello");
        System.out.println(map.size());
    }

    public static void concurrentHashMapTest() {
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("name", "hello");
    }

    public static void main(String[] args) {
//        hashMapTest();
//        linkedHashMapTest();
        hashTableTest();
    }
}
