package com.fader.vnote.io.socket;



import java.io.*;
import java.net.*;

/**
 * @author FaderW
 * 2019/8/2
 */

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(30000);

        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 2000));

        System.out.println("客户端连接成功");
        System.out.println("服务端" + socket.getInetAddress() + "P:" + socket.getPort());
        System.out.println("客户端" + socket.getLocalAddress() + "P:" + socket.getLocalPort());

        handle(socket);

    }

    public static void handle(Socket client) throws IOException {
        InputStream in = System.in;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        boolean flag = true;
        try (OutputStream outputStream = client.getOutputStream();
             InputStream inputStream = client.getInputStream()) {

            PrintStream printStream = new PrintStream(outputStream);
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(inputStream));
            while (flag) {
                String msg = reader.readLine();
                printStream.println(msg);

                String echo = socketReader.readLine();
                if ("bye".equalsIgnoreCase(echo)) {
                    flag = false;
                } else {
                    System.out.println(echo);
                }
            }
        } finally {
            System.out.println("客户端已关闭");
        }
    }
}
