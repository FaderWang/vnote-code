package com.fader.vnote.javabase.clone;

import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author FaderW
 * 浅拷贝
 * 2019/6/12
 */
@NoArgsConstructor
public class Person implements Cloneable, Serializable {

    private static final long serialVersionUID = -4263621926233270807L;
    public int age;
    public String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }


    public static void main(String[] args) throws CloneNotSupportedException {
        Person p1 = new Person(20, "james");
        Person p2 = (Person) p1.clone();

        System.out.println(p1.age);
        System.out.println(p2.age);
    }
}
