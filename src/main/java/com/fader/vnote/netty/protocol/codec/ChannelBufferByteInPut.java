package com.fader.vnote.netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

/**
 * @author FaderW
 * 2019/8/27
 */

public class ChannelBufferByteInPut implements ByteInput {

    private final ByteBuf byteBuf;

    public ChannelBufferByteInPut(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }


    @Override
    public int read() throws IOException {
         if (byteBuf.isReadable()) {
             return byteBuf.readByte() & 0xff;
         }
         return -1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }
        int length = Math.min(available, len);
        byteBuf.readBytes(b, off, length);
        return length;
    }

    @Override
    public int available() throws IOException {
        return byteBuf.readableBytes();
    }

    @Override
    public long skip(long n) throws IOException {
        int available = available();
        if (available < n) {
            n = available;
        }
        byteBuf.readerIndex((int) (byteBuf.readerIndex() + n));
        return n;
    }

    @Override
    public void close() throws IOException {

    }
}
