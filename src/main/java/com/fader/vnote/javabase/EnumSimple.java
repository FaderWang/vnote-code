package com.fader.vnote.javabase;

import java.io.*;

/**
 * @author FaderW
 */
public class EnumSimple {

    public enum Season {
        SPRING;
    }

    public static void main(String[] args) {
        /**
         * 枚举enum是一种语法糖，反编译枚举类后可以发现是一个继承了Enum的final类，里面定义的枚举值都是一个该枚举类
         * 类型的常量，所以枚举类的所有的值在枚举类初始化的时候就被赋值了。是线程安全的。
         */
        // 枚举序列化只会序列化name属性值
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("objFile"))){
            oos.writeObject(Season.SPRING);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 枚举类可以防止反序列化时利用反射破坏单例模式
         * 原理: 枚举反序列化时调用Enum.valueOf方法，接着反射调用枚举类的values方法，或者所有枚举值，在根据name去匹配对应的枚举
         * 这里不会生成新的对象。
         *
         * 也可以通过重写readResolve方法防止反序列化破坏单例
         */
        File file = new File("objFile");
        System.out.println(file.getAbsolutePath());
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            Season season = (Season) ois.readObject();
            System.out.println(season == Season.SPRING);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
