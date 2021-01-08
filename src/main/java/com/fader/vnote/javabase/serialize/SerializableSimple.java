package com.fader.vnote.javabase.serialize;

import java.io.*;
import java.util.Arrays;

/**
 * @author FaderW
 * 2020/11/23
 */

public class SerializableSimple {

    public static class User implements Serializable {

        private static final long serialVersionUID = -7506726678084694518L;
        private String name;
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
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
