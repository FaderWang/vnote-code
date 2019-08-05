package com.fader.vnote.collection.list;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
//        deleteTest();
//        sublistTest();
        List<String> list = new ArrayList<>(0);
        list.add("name");
    }


}
