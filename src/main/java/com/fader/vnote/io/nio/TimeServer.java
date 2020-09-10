package com.fader.vnote.io.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

/**
 * @author FaderW
 * 2019/10/18
 */

public class TimeServer implements Runnable{

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public TimeServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                int n = selector.select(1000);
                if (n == 0) {
                    continue;
                }
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    handle(selectionKey);
                    iterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
            try {
                int rs = socketChannel.read(byteBuffer);
                System.out.println("read rs :" + rs);
                if (rs != -1) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    System.out.println("remaining size :" + bytes.length);
                    byteBuffer.get(bytes);

                    String response = new String(bytes);
                    if (response.startsWith("TIME ORDER")) {
                        doWrite(socketChannel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String msg = now.format(DateTimeFormatter.BASIC_ISO_DATE);

        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
    }

    public static void main(String[] args) {
        new Thread(new TimeServer(8888)).start();
    }
}
