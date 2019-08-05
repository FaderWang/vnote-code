package com.fader.vnote.io;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author FaderW
 * 2019/7/30
 */

public class SimpleIO {

    public static void read() throws IOException {
//        File file = new File("a.txt");
        String filePath = SimpleIO.class.getClassLoader().getResource("com/fader/vnote/io/a.txt").getFile();
        FileInputStream fileInputStream = new FileInputStream(filePath);

        byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = fileInputStream.read(bytes)) > 0) {
            System.out.println(new String(bytes));
        }
    }

    public static void main(String[] args) throws IOException {
        read();

    }
}
