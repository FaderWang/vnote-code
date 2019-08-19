package com.fader.vnote.io.chat;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author FaderW
 * 2019/8/16
 */

public class ChatClient {

    public void start() throws IOException {
        Socket client = new Socket();
        client.connect(new InetSocketAddress("127.0.0.1", 30124));
        System.out.println("access to chat room");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


        try (OutputStream out = client.getOutputStream();
            InputStream in = client.getInputStream()){

            new Thread(new ServerListener(in)).start();

            while (true) {
                String msg = reader.readLine();
                out.write((msg + "\n").getBytes());
                out.flush();
            }
        }
    }

    class ServerListener implements Runnable {
        InputStream inputStream;

        public ServerListener(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            while (true) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                try {
                    String msg = reader.readLine();
                    System.out.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
