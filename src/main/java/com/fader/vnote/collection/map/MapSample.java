package com.fader.vnote.collection.map;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FaderW
 * 2019/7/25
 */

public class MapSample {

    public static void hashMapTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("age", 10);

    }

    public static void linkedHashMapTest() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("age", "20");
    }

    public static void main(String[] args) {
//        hashMapTest();
        linkedHashMapTest();
    }
}
