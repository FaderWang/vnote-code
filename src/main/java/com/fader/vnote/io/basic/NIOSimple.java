package com.fader.vnote.io.basic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author FaderW
 * 2020/2/10
 */

public class NIOSimple {

    static void fileRead() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("io-simple.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(48);

        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            System.out.print((char) byteBuffer.get());
        }
    }

    public static void main(String[] args) throws IOException {
        fileRead();
    }
}
