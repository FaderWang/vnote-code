package com.fader.vnote.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author FaderW
 * 2019/8/21
 */

public class ChannelDrain {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9090));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(null);
        SocketChannel socketChannel = serverSocketChannel.accept();
    }
}
