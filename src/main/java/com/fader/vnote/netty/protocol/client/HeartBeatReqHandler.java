package com.fader.vnote.netty.protocol.client;

import com.fader.vnote.netty.protocol.MessageType;
import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author FaderW
 * 2019/8/28
 */

public class HeartBeatReqHandler extends ChannelHandlerAdapter {

    private volatile ScheduledFuture heartBeat;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            //握手成功，心跳检测
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx),
                    0, 5000, TimeUnit.MILLISECONDS);
        } else if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            System.out.println("Client receive heart beat message from server : ----> " + nettyMessage);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Slf4j
    private static class HeartBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            log.info("Client send heart beat messsage to server : ---> "
                    + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildHeatBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }
}
