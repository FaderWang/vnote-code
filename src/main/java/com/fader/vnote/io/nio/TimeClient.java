package com.fader.vnote.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author FaderW
 * 2019/10/18
 */

public class TimeClient implements Runnable {

    private SocketChannel socketChannel;
    private Selector selector;
    private int port;

    public TimeClient(int port) {
        try {
            this.port = port;
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                int n = selector.select(1000);
                if (n == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handle(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite();
                }
            } else if (key.isReadable()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(48);
                int rs = socketChannel.read(byteBuffer);
                if (rs != -1) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String response = new String(bytes);

                    System.out.println("Client receive response from server : " + response);
                } else {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        System.out.println("do connect...");
        /**
         * 非阻塞模式下connect会立即返回
         * 返回true连接成功，返回false会并发的重新进行连接
         */
        if (socketChannel.connect(new InetSocketAddress("127.0.0.1", port))) {
            System.out.println("succeed connect");
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite();
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite() throws IOException {
        String msg = "TIME ORDER REQUEST";
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
        System.out.println("send order to server succeed!");
    }

    public static void main(String[] args) {
        TimeClient timeClient = new TimeClient(8888);
        Thread thread = new Thread(timeClient);
        thread.start();
    }
}
