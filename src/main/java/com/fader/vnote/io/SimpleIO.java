package com.fader.vnote.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author FaderW
 * 2019/7/30
 */

public class SimpleIO {

    public static void read() throws IOException {
//        File file = new File("a.txt");
        String filePath = SimpleIO.class.getClassLoader().getResource("test.txt").getFile();
        System.out.println(filePath);
        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedInputStream inputStream = new BufferedInputStream(fileInputStream);

        byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = inputStream.read(bytes)) > 0) {
            System.out.println(new String(bytes, 0, read));
        }

    }

    public void test() {
        Object o = new Object();
    }

    public static void main(String[] args) throws IOException {
        read();

    }
}
