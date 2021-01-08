package com.fader.vnote.collection.list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.IntStream;

/**
 * @author FaderW
 * 2019/7/24
 */

public class ListSample {

    private static final List<String> STRING_LIST = Lists.newArrayList("dog", "cat", "pig");

    public static void deleteTest() {
        //增强for循环底层用的迭代器，删除后内部modCount != expectedModCount
        for (String s: STRING_LIST) {
            STRING_LIST.remove(s);
        }

        //遍历可以删除，但是下标要自减，不然会漏掉
        for (int i = 0; i < STRING_LIST.size(); i++) {
            System.out.println(STRING_LIST.get(i));
            STRING_LIST.remove(i);
            i--;
        }
    }

    public static void sublistTest() {
        //subList返回原集合的视图，父子集合的改变影响对方。
        //父集合增加或删除元素，子集合的增加或删除会抛异常
        List<String> subList = STRING_LIST.subList(0, 2);

        subList.add("duck");
        STRING_LIST.add("wolf");
    }

    public static void arrayToList() {
        // 使用Arrays.asList方法放回的ArrayList是Arrays的内部类，不能新增。
        // 内部直接保存的外部数组的引用，所以数组改变会影响list
        Integer[] array = new Integer[]{1, 2, 3, 4};
        List<Integer> list = Arrays.asList(array);
//        list.add(3); 或抛出UnsupportedOperationException异常
        array[0] = 10;
        System.out.println("nums" + (int)list.get(0));
    }

    public static void capacity() throws JsonProcessingException {
        // 指定容量为0的化，会指向一个空数组，改空数组是属于静态属性，只有一个，所有空集合都执行它。
        // 不指定容量，默认为10。采用懒加载思想，add第一个元素为初始化容量
        List<String> list = Lists.newArrayListWithCapacity(0);
        System.out.println(list.size());
        System.out.println(new ObjectMapper().writeValueAsString(list));
//        list.set(10, "")
    }

    public static void serialize() throws JsonProcessingException {
        // 重写了序列化，根据当前size即元素的个数序列化，而不是容量
        List<String> list = Lists.newArrayListWithCapacity(60);
        list.add("a");
        list.add("b");
        list.add(null);
        list.set(0, null);
        System.out.println(new ObjectMapper().writeValueAsString(list));
    }

    public static void vectorTest() {
        // 每次扩容为2倍，可以在创建实例时设置capacityIncrement指定每次扩容增加的容量
        Vector<String> vector = new Vector<>();
        for (int i = 0; i < 10; i++) {
            vector.add(i + "");
        }
        System.out.println(vector.size());
        vector.add("11");
        System.out.println(vector.size());
    }

    public static void main(String[] args) throws JsonProcessingException {
//        deleteTest();
//        sublistTest();
//        List<String> list = new ArrayList<>(0);
//        list.add("name");
//        arrayToList();
//        capacity();
//        serialize();
        vectorTest();

    }


}
