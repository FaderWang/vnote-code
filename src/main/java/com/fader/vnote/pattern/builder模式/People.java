package com.fader.vnote.pattern.builder模式;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author FaderW
 * 2019/4/19
 */
@Getter
@Setter
@Builder(toBuilder = true)
public class People {


    private String sex;

    private String name;

    @Builder.ObtainVia(field = "age")
    private int age;

    public static void main(String[] args) {
        People people = People.builder().age(10).name("wang").build();

        People people1 = people.toBuilder().build();
        System.out.println(people.getAge());
        System.out.println(people.getName());
        System.out.println(people1.getAge());
        System.out.println(people1.getName());
    }
}
