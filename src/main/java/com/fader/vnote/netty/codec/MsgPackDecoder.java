package com.fader.vnote.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @author FaderW
 * 2019/8/22
 */

public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf>{
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        MessagePack messagePack = new MessagePack();
        list.add(messagePack.read(bytes));
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
    }
}
