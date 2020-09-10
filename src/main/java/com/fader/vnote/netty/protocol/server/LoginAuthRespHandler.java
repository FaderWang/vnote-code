package com.fader.vnote.netty.protocol.server;

import com.fader.vnote.netty.protocol.MessageType;
import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author FaderW
 * 2019/8/28
 */
@Slf4j
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    private String[] whiteList = {"127.0.0.1"};


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage = (NettyMessage) msg;
        if (nettyMessage.getHeader() != null &&
                nettyMessage.getHeader().getType() == MessageType.LOGIN_REQ.value()) {

            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            String ip = inetSocketAddress.getAddress().getHostAddress();
            boolean isOk = false;

            for (String s : whiteList) {
                if (s.equals(ip)) {
                    isOk = true;
                    break;
                }
            }
            NettyMessage loginResp = isOk ? buildResp((byte) 0) : buildResp((byte) -1);
//            log.info("login resp : " + loginResp);
            System.out.println("login resp : " + loginResp);
            ctx.writeAndFlush(loginResp);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResp(byte result) {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();

        header.setType(MessageType.LOGIN_RESP.value());
        nettyMessage.setHeader(header);
        nettyMessage.setBody(result);

        return nettyMessage;
    }
}
