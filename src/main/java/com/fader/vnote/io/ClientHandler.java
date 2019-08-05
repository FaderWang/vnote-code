package com.fader.vnote.io;

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

public class ClientHandler extends Thread {

    private String host;
    private Integer port;
    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop = false;

    public ClientHandler(String host, Integer port) {
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
                selector.select(1000);
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
//                SocketChannel socketChannel = (SocketChannel) key.channel();
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
        ClientHandler client = new ClientHandler("127.0.0.1", 8888);
        client.start();
    }

}
