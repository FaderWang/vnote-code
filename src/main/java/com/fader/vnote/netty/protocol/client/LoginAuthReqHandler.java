package com.fader.vnote.netty.protocol.client;

import com.fader.vnote.netty.protocol.MessageType;
import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author FaderW
 * 2019/8/28
 */
@Slf4j
public class LoginAuthReqHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    private NettyMessage buildLoginReq() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();

        header.setType(MessageType.LOGIN_REQ.value());
        nettyMessage.setHeader(header);

        return nettyMessage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (byte) nettyMessage.getBody();
            if (loginResult == (byte) 0) {
//                log.info("Login is ok : " + nettyMessage);
                System.out.println("Login is ok : " + nettyMessage);
                ctx.fireChannelRead(msg);
            } else {
                ctx.close();
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

}
