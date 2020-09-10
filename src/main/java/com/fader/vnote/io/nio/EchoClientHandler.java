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
 * 2019/4/18
 */

public class EchoClientHandler extends Thread {

    private String host;
    private Integer port;
    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop = false;

    public EchoClientHandler(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
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
            System.exit(1);
        }
        while (!stop) {
            try {
                int n = selector.select(1000);
                if (n == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    handle(key);
                    iterator.remove();
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
                    this.stop = true;
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
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            System.out.println("succeed connect");
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite();
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite() throws IOException {
        String msg = "Hello EchoServer";
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
        System.out.println("send order to server succeed!");
    }

    public static void main(String[] args) {
        EchoClientHandler client = new EchoClientHandler("127.0.0.1", 8888);
        client.start();
    }

}
