package com.fader.vnote.javabase;

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
        // jdk1.7中常量池转移到堆中，当堆中存在该字符串对象时，常量池直接存储的是该对象的引用
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

    public static void main(String[] args) {
//        constantPool();
        stringImmutable();
    }
}
