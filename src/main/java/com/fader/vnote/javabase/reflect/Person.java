package com.fader.vnote.javabase.reflect;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author FaderW
 * 2019/6/11
 */
@Getter
@Setter
public class Person {

    private String name;
    private int age;

    public void run() {
        System.out.println("run");
    }

    public static void main(String[] args) {
        Class classz = null;
        try {
            classz = Class.forName("com.fader.vnote.javabase.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method[] methods = classz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.toString());
        }

        Field[] fields = classz.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.toString());
        }
        try {
            Method method = classz.getDeclaredMethod("run");
            System.out.println(method.toString());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
