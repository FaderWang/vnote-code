package com.fader.vnote.javabase.clone;

import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author FaderW
 * 浅拷贝
 * 2019/6/12
 */
@NoArgsConstructor
public final class Person implements Cloneable, Serializable {

    private static final long serialVersionUID = -4263621926233270807L;
    public int age;
    public String name;

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }


//    public static void main(String[] args) throws CloneNotSupportedException {
//        Person p1 = new Person(20, "james");
//        Person p2 = (Person) p1.clone();
//
//        System.out.println(p1.age);
//        System.out.println(p2.age);
//    }

    public int get() {
        return 0;
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//       int running = -1 << 29;
//        AtomicInteger atomicInteger = new AtomicInteger(running | 0);
//        System.out.println(atomicInteger.get());
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
////        executorService.execute(() ->
////            System.out.println("test"));
//
//        System.out.println(! (true && false));
//        Thread.currentThread().isInterrupted();
//        Thread.currentThread().interrupt();
        Human human = new Human();
        human.setName("faderW");
        Method method = Object.class.getDeclaredMethod("clone");
        method.setAccessible(true);
        Human human1 = (Human) method.invoke(human);
//        System.out.println(human == human1);
        System.out.println(human1.getName());
    }
}

