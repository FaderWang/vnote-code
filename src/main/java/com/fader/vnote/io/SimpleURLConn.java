package com.fader.vnote.io;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author FaderW
 * 2019/8/1
 */

public class SimpleURLConn {

    public static void simpleRequest() throws IOException{
        URL url = new URL("https://www.baidu.com");
        URLConnection connection = url.openConnection();
        try (InputStream raw = connection.getInputStream()) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(raw);
            Reader reader = new InputStreamReader(bufferedInputStream);
            char[] buffer = new char[bufferedInputStream.available()];
            reader.read(buffer);
            for (char chr : buffer) {
                System.out.print(chr);
            }
        }
    }

    public static void saveBinaryFile() {

    }

    public static void doPost() {

    }
}
