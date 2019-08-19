package com.fader.vnote.io.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author FaderW
 * 2019/8/16
 */

public class ChatServer {

    private static final String END_MSG = "end";
    private volatile boolean stop = false;
    private AtomicInteger client_count;
    private ExecutorService client_pool;

    private final List<Socket> clientList;

    public ChatServer() {
        client_count = new AtomicInteger();
        client_pool = new ThreadPoolExecutor(20, 20, 60L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(1024));

        clientList = new ArrayList<>();
    }



    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(30124);
        while (!stop) {
            Socket socket = serverSocket.accept();
            clientList.add(socket);
            incrementCount();
            client_pool.execute(new ClientHandler(socket, new CallBack() {

                @Override
                public void onMessageReceived(String msg) {
                    synchronized (ChatServer.this) {
                        for (Socket client : clientList) {
                            try {
                                OutputStream outputStream = client.getOutputStream();
                                outputStream.write(msg.getBytes());
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public synchronized void onClientExit(Socket current) {
                    Iterator<Socket> iterator = clientList.iterator();
                    while (iterator.hasNext()) {
                        Socket client = iterator.next();
                        if (client == current) {
                            iterator.remove();
                            try {
                                System.out.println("客户端" + current.getInetAddress() + "退出");
                                current.close();
                                decrementCount();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    sendExitMsg(current);
                }

                private void sendExitMsg(Socket current) {
                    for (Socket client: clientList) {
                        try {
                            OutputStream outputStream = client.getOutputStream();
                            StringBuilder msg = new StringBuilder("客户端")
                                    .append(current.getLocalAddress())
                                    .append(":").append(current.getLocalPort())
                                    .append("退出");
                            outputStream.write(msg.toString().getBytes());
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }));
        }

        serverSocket.close();
        System.out.println("聊天室关闭");
    }


    private void incrementCount() {
        client_count.incrementAndGet();
    }

    private void decrementCount() {
        int count = client_count.decrementAndGet();
        if (count == 0) {
            stop = true;
        }
    }




    interface CallBack {

        void onMessageReceived(String msg);

        void onClientExit(Socket current);
    }


    class ClientHandler implements Runnable {

        Socket socket;
        CallBack callBack;

        public ClientHandler(Socket socket, CallBack callBack) {
            this.socket = socket;
            this.callBack = callBack;
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream()) {
                while (true) {
                    byte[] bytes = new byte[8102];
                    int len = in.read(bytes);
                    String recv = new String(bytes, 0, len);

                    if (recv.contains(END_MSG)) {
                        callBack.onClientExit(socket);
                        break;
                    } else {
                        callBack.onMessageReceived(recv);
                    }
                }

            } catch (IOException e) {
                System.out.println(socket.getInetAddress() + ":" + socket.getLocalPort() + "发生异常，客户端将退出");
                callBack.onClientExit(socket);
            }

        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        try {
            chatServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
