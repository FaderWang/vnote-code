package com.fader.vnote.netty.protocol.codec;

import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;


/**
 * @author FaderW
 * 2019/8/27
 */

public class NettyMessageEncoder extends
        MessageToByteEncoder<NettyMessage> {

    private MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }
        Header header = msg.getHeader();
        out.writeInt(header.getCrcCode());
        out.writeInt(header.getLength());
        out.writeLong(header.getSessionId());
        out.writeByte(header.getType());
        out.writeByte(header.getPriority());
        out.writeInt(header.getAttachment().size());

        String key;
        byte[] keyArr;
        Object value;
        for (Map.Entry<String, Object> entry : header.getAttachment().entrySet()) {
            key = entry.getKey();
            keyArr = key.getBytes("UTF-8");
            out.writeInt(keyArr.length);
            out.writeBytes(keyArr);
            value = entry.getValue();
            marshallingEncoder.encode(value, out);
        }

        key = null;
        keyArr = null;
        value = null;
        if (null != msg.getBody()) {
            marshallingEncoder.encode(msg.getBody(), out);
        } else {
            out.writeInt(0);
        }
        out.setInt(4, out.readableBytes() - 8);
    }

}
