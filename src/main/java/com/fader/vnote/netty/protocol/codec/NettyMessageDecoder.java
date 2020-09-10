package com.fader.vnote.netty.protocol.codec;


import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.Map;


/**
 * @author FaderW
 * 2019/8/27
 */
public class NettyMessageDecoder extends
        LengthFieldBasedFrameDecoder {

    private MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();

        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionId(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());

        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attachment = Maps.newHashMapWithExpectedSize(size);
            String key;
            byte[] keyArr;
            Object value;
            for (int i = 0; i < size; i ++) {
                keyArr = new byte[frame.readInt()];
                frame.readBytes(keyArr);
                key = new String(keyArr, "UTF-8");
                value = marshallingDecoder.decode(frame);

                attachment.put(key, value);
            }
            key = null;
            keyArr = null;
            value = null;
            header.setAttachment(attachment);
        }
        if (frame.readableBytes() > 4) {
            Object body = marshallingDecoder.decode(frame);
            nettyMessage.setBody(body);
        }
        nettyMessage.setHeader(header);

        return nettyMessage;
    }

}
