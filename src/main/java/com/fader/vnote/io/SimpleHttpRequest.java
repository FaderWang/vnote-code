package com.fader.vnote.io;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.*;

/**
 * @author FaderW
 * 2019/8/1
 */

public class SimpleHttpRequest {

    private static final String BOUNDARY = "WebKitFormBoundary7MA4YWxkTrZu0gW";

    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary="
            + BOUNDARY;

    public static void get() {
        try {
            URL url = new URL("http://httpbin.org/get");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 打印首部
            for (int i = 1; ;i++) {
                String header = connection.getHeaderField(i);
                if (null == header) {
                    break;
                }
                System.out.println(connection.getHeaderFieldKey(i) + ": " + header);
            }
            //读取数据
            try (InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
            {
                BufferedInputStream buffer = new BufferedInputStream(inputStream);
                byte[] bytes = new byte[80320];
                int c = 0;
                while ((c = buffer.read(bytes)) != -1 ) {
                        outputStream.write(bytes, 0, c);
                }
                System.out.print(outputStream.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void post() {
        try {
            URL url = new URL("http://httpbin.org/post");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            try (OutputStream out = connection.getOutputStream()) {

                Writer writer = new OutputStreamWriter(new BufferedOutputStream(out));
                // 传递json
                JSONObject object = new JSONObject();
                object.put("age", URLEncoder.encode("20"));
                object.put("name", URLEncoder.encode("faderw"));
                writer.write(object.toJSONString());

                writer.flush();

                try (InputStream in = connection.getInputStream();
                     ByteArrayOutputStream outputStream = new ByteArrayOutputStream())
                {
                    BufferedInputStream buffer = new BufferedInputStream(in);
                    byte[] bytes = new byte[2056];
                    int c = 0;
                    while ((c = buffer.read(bytes)) != -1 ) {
                        outputStream.write(bytes, 0, c);
                    }
                    System.out.print(outputStream.toString());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void multipart() {
        try {
            URL url = new URL("http://httpbin.org/post");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", CONTENT_TYPE_MULTIPART);

            connection.setDoOutput(true);
            try (OutputStream outputStream = connection.getOutputStream()) {
                Writer writer = new OutputStreamWriter(outputStream);
                File file = new File("/Users/faderw/source/a.txt");
                System.out.print(file.length());
                writer.write("--" + BOUNDARY + "\r\n");
                writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"a.txt\"");
                writer.write("\r\n");
                try (Reader reader = new InputStreamReader(new FileInputStream(file.getAbsolutePath()))){
                    int c = 0;
                    char[] chars = new char[8024];
                    while ((c = reader.read(chars)) != -1) {
                        writer.write(chars, 0, c);
                    }
                    writer.write("\r\n");
                    writer.write("\r\n" + "--" + BOUNDARY + "--");
                }
                writer.flush();

                try (InputStream in = connection.getInputStream();
                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
                {
                    BufferedInputStream buffer = new BufferedInputStream(in);
                    byte[] bytes = new byte[2056];
                    int c = 0;
                    while ((c = buffer.read(bytes)) != -1 ) {
                        byteArrayOutputStream.write(bytes, 0, c);
                    }
                    System.out.print(byteArrayOutputStream.toString());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void proxy() throws IOException {
//        Authenticator.setDefault(new LoginAuthenticator(null, "Wang127198"));
        SocketAddress address = new InetSocketAddress("127.0.0.1", 1086);
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);

        URL url = new URL("http://www.google.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        System.out.print(connection.getResponseCode());
    }

    static class LoginAuthenticator extends Authenticator {
        /** User name **/
        private String m_sUser;
        /** Password **/
        private String m_sPsw;

        public LoginAuthenticator(String sUser, String sPsw) {
            m_sUser = sUser;
            m_sPsw = sPsw;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(m_sUser, m_sPsw.toCharArray()));
        }
    }

    public static void main(String[] args) throws IOException {
//        get();
//        post();
//        multipart();
        proxy();
    }
}
