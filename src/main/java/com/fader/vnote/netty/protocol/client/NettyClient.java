package com.fader.vnote.netty.protocol.client;

import com.fader.vnote.netty.protocol.codec.NettyMessageDecoder;
import com.fader.vnote.netty.protocol.codec.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author FaderW
 * 2019/8/28
 */

public class NettyClient {

    public void go() {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("nettyMessageDecoder", new NettyMessageDecoder(1024 * 1024, 4, 4));
                            pipeline.addLast("nettyMessageEncoder", new NettyMessageEncoder());
                            pipeline.addLast("loginReqHandler", new LoginAuthReqHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.go();
    }
}
