package com.fader.vnote.netty.protocol.server;

import com.fader.vnote.netty.protocol.MessageType;
import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author FaderW
 * 2019/8/28
 */

public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()) {
            System.out.println("receive heart beat message from client :----> " + nettyMessage);
            ctx.writeAndFlush(buildHeartBeatResp());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeatResp() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        nettyMessage.setHeader(header);

        return nettyMessage;
    }
}
