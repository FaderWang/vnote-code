package com.fader.vnote.javabase;

import java.io.*;

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
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tempFile"));){
            oos.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
