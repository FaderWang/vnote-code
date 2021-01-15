package com.fader.vnote.javabase.serialize;

import java.io.*;
import java.util.Arrays;

/**
 * @author FaderW
 * 2020/11/23
 */

public class SerializableSimple {

    public static class User implements Serializable {

        /**应当显示定义一个serialVersionUID，反序列化时除了取决于类路径和功能代码是否一致，还会比较应当显示定义一个serialVersionUID。
         * 如果字节流中的serialVersionUID与JVM中的不一致，则会抛出异常InvalidCastException。
         * 如果不显示指定改ID，虚拟机会自动生成一个，但是如果类发生改变，重新编译后生成的Id也会不一样，此时反序列化会失败。
         *
         * 类新增属性时，如果想兼容升级，则不修改serialVersionUID，如果不兼容升级，则可以修改serialVersionUID。
         */
        private static final long serialVersionUID = -7506726678084694518L;
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    /**
     * 继承Externalizable，必须重写writeExternal，并自定义需要序列化属性的逻辑
     */
    public static class Order implements Externalizable {


        private String name;

        private int amount;

        // 继承Externalizable 必须有一个无参构造函数，否则会抛出no valid constructor的异常
        public Order() {

        }

        public Order(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }


        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(name);
            out.writeObject(amount);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            name = (String) in.readObject();
            amount = in.readInt();
        }
    }

    public static void main(String[] args) {
        User user = new User("faderw", 26);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);){
            oos.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // https://www.cnblogs.com/binarylei/p/10987933.html序列化源码分析
        System.out.println(Arrays.toString(byteArrayOutputStream.toByteArray()));

        File file = new File("tempFile");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            User user1 = (User) ois.readObject();
            System.out.println(user1.name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
