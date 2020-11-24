package com.fader.vnote.javabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author FaderW
 * 2019/12/23
 */

public class StringSimple {

    static void constantPool() {
//        String s = new String("1");
//        s = s.intern();
//        String s1 = "1";
//        System.out.println(s == s1);
        // jdk1.7中常量池转移到堆中，当堆中存在该字符串对象时，调用string#intern放入常量池时，常量池直接存储的是该对象的引用
        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4);
//        String.valueOf()
    }

    static void stringBuilder() {

    }

    static void stringImmutable() {
        String s = "kobe";
        String s1 = s;
        String s2 = s;
        s1 = "james";
        System.out.println(s2);
    }

    static void jdkSubstring() {
        String s = new String(new byte[1000000]);
        // jdk1.6 substring会共享同一个char[]数组，当char数组很大，虽然substring截取的长度很小时，但是会有引用存在，char数组得不得回收。
        // jdk1.7 修复了这个问题，new string()构造时，会复制一个新数组。
        s.substring(0, 2);
    }

    static void stringReplace() {
        Pattern pattern = Pattern.compile("正则表达式");
        Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World");
        //替换第一个符合正则的数据
        System.out.println(matcher.replaceAll("Java"));
    }

    static void string2int() {
        int i = 5;
        String.valueOf(i);
        Integer.toString(i);
    }

    public static void main(String[] args) {
//        constantPool();
//        stringImmutable();
//        System.out.println(Integer.MAX_VALUE);
//        System.out.println(Integer.MAX_VALUE+1);
//        System.out.println(Integer.MAX_VALUE+2);
//        System.out.println(Integer.MAX_VALUE+1 - (Integer.MAX_VALUE+2));
        stringReplace();
    }
}
