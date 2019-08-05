package com.fader.vnote.io.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author FaderW
 * 2019/8/2
 */

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2000);
        System.out.println("服务器已准备就绪~~~");
        for (;;) {
            Socket socket = server.accept();
            System.out.println("客户端已连接");
            new ClientHandler(socket).start();
        }
    }


    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = socket.getInputStream();
                 OutputStream outputStream = socket.getOutputStream()) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                PrintStream printStream = new PrintStream(outputStream);

                while (flag) {
                    String recv = reader.readLine();
                    if ("bye".equalsIgnoreCase(recv)) {
                        flag = false;
                        printStream.println("bye");
                    } else {
                        System.out.println(recv);
                        printStream.println("回送" + recv.length());
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("客户端断开连接");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
