package com.fader.vnote.javabase.clone;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

/**
 * @author FaderW
 * 深拷贝有两种方法
 * 1.引用变量也进行拷贝
 * 2.实现序列化接口，先序列化，在反序列化
 * 2019/6/12
 */
@Getter
@Setter
public class Human implements Cloneable, Serializable{

    private static final long serialVersionUID = -7358098955877210939L;
    private String name;
    private int age;

    private Person person;


    public static void main(String[] args) throws CloneNotSupportedException, IOException, ClassNotFoundException {
        Human human = new Human();
        Person person = new Person();
        human.setAge(20);
        human.setName("Lebron");
        human.setPerson(person);

//        Human human1 = (Human) human.clone();
//        System.out.println(human + "," + human1);
//        System.out.println(person + "," + human1.getPerson());


        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            objectOutputStream.writeObject(human);
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            ){
                Human human1 = (Human) objectInputStream.readObject();
                System.out.println(human + "," + human1);
                System.out.println(person + "," + human1.getPerson());
            }

        }
    }
}
