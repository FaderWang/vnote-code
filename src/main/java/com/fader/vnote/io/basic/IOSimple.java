package com.fader.vnote.io.basic;

import java.io.*;

/**
 * @author FaderW
 * 2020/2/9
 */

public class IOSimple {

    static void fileReadAndWrite() throws IOException {
        File file = new File("io-simple.txt");
        FileOutputStream outputStream = new FileOutputStream(file);
        String content = "Hello,FaderW";
        outputStream.write(content.getBytes());

        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        System.out.println(new String(bytes));

    }

    static void pipedReadAndWrite() throws IOException {
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write("Hello,FaderW".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                int data = inputStream.read();
                while (data != -1) {
                    System.out.print((char) data);
                    data = inputStream.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
    }

    static void arrayReadAndWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write("Hello,FaderW".getBytes());

        System.out.println(out.toString());
    }

    static void bufferReadAndWrite() throws FileNotFoundException {
    }

    public static void main(String[] args) {
        try {
//            fileReadAndWrite();
//            pipedReadAndWrite();
            arrayReadAndWrite();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
