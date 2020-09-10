package com.fader.vnote.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author FaderW
 * 2019/8/22
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends ChannelHandlerAdapter {

    private int count = 0;
    private static final String ECHO_MSG = "Hello, Netty$_";

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 5; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_MSG.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("This is " + ++count + " times receive server [" + msg + "]");
    }
}
