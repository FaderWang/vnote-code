package com.fader.vnote.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author FaderW
 * 2019/3/18
 */

public class EchoServerHandler extends Thread{

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop = false;

    public EchoServerHandler(Integer port) {
        try {
            //开启serverSocketChannel
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
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                //迭代selectionKey
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handle(selectionKey);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key) throws IOException {
        System.out.println("server handle key : " + key.interestOps());
        if (key.isAcceptable()) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            //接受socket连接，获取连接的socketChannel,注册读事件，以便下次select读取客户端发送的内容
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);
            int rs = socketChannel.read(byteBuffer);
            if (rs != -1) {
                //切换模式
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                String msg = new String(bytes, "UTF-8");
                System.out.println("The echoServer receive msg : " + msg);
                if (msg != null) {
                    doWrite(socketChannel, msg);
                    key.cancel();
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String response) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);
        byteBuffer.put(response.getBytes());
        byteBuffer.flip();

        socketChannel.write(byteBuffer);
    }

    public static void main(String[] args) {
        EchoServerHandler server = new EchoServerHandler(8888);
        server.start();
    }
}
