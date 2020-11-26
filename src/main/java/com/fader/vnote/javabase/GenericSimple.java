package com.fader.vnote.javabase;

import com.fader.vnote.jvm.exception.Foo;

import java.util.List;

/**
 * @author FaderW
 */
public class GenericSimple {

    public static class Food {

    }

    public static class Fruit extends Food {

    }

    public static class Apple extends Fruit {

    }

    public static class Banana extends Fruit {

    }

    static void testExtend(List<? extends Fruit> list) {
        // extends为上界通配符，只能取值，不能放。因为这里不能确定具体的泛型到底是Apple还是Banana，所以放入任何一种类型都会报错
//        list.add(new Apple());
//        list.add(new Banana())
        list.get(0);
    }

    static void superExtend(List<? super Fruit> list) {
        list.get(0);
        list.add(new Apple());
        list.add(new Banana());
        // super为下界通配符，表示类型为Fruit的基类，所以存储Fruit或者Fruit的子类都没问题，但是不能添加父类，因为不能确定具体的父类是哪种类型。
//        list.add(new Foo());

        // pecs原则
        // https://www.zhihu.com/question/20400700

    }
}
