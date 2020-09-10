package com.fader.vnote.io.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

/**
 * @author FaderW
 * 2019/8/20
 */

public class BufferDrain {

    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(100);
        ByteBuffer.allocate(1024);
        ByteBuffer.allocateDirect(1024);
        charBuffer.put('c').put('a').put('z').put('X');

        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());

        /**
         * limit = position; position = 0; mark = -1
         * make buffer ready for ready
         */
        charBuffer.flip();
        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());

        System.out.println(charBuffer.get());
        System.out.println(charBuffer.get());
        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());


        /**
         * clear()和compact()的区别
         * clear将position移到0，limit=capacity，并没有真正删除数据
         * compact保留未读取的数据，并复制到首部，并将position指向下一个位置
         */
        charBuffer.clear();
        charBuffer.compact();
        System.out.println(charBuffer.position());
        System.out.println(charBuffer.limit());
    }

    public static void Scatter() throws IOException {
        RandomAccessFile file = new RandomAccessFile("", "");
        FileChannel fileChannel = file.getChannel();
        ByteBuffer header = ByteBuffer.allocateDirect(1024);
        ByteBuffer body = ByteBuffer.allocateDirect(8102);
        ByteBuffer[] buffers = {header, body};

        fileChannel.read(buffers);
    }
}
