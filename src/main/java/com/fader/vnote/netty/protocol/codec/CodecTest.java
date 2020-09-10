package com.fader.vnote.netty.protocol.codec;

import com.fader.vnote.netty.protocol.MessageType;
import com.fader.vnote.netty.protocol.struct.Header;
import com.fader.vnote.netty.protocol.struct.NettyMessage;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author FaderW
 * 2019/8/28
 */

public class CodecTest {

    MarshallingEncoder marshallingEncoder;
    MarshallingDecoder marshallingDecoder;

    public CodecTest() throws IOException {
        marshallingEncoder = new MarshallingEncoder();
        marshallingDecoder = new MarshallingDecoder();
    }

    public NettyMessage buildMessage() {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();
        header.setLength(123);
        header.setType(MessageType.SERVICE_REQ.value());
        header.setSessionId(66666);
        header.setPriority((byte) 7);

        Map<String, Object> attachment = Maps.newHashMap();
        for (int i = 0; i < 5; i++) {
            attachment.put("num" + i, i);
        }
        header.setAttachment(attachment);
        nettyMessage.setHeader(header);
        nettyMessage.setBody("Hello, this is netty message");

        return nettyMessage;
    }

    public ByteBuf encode(NettyMessage nettyMessage) throws Exception {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(nettyMessage.getHeader().getCrcCode());
        byteBuf.writeInt(nettyMessage.getHeader().getLength());
        byteBuf.writeLong(nettyMessage.getHeader().getSessionId());
        byteBuf.writeByte(nettyMessage.getHeader().getType());
        byteBuf.writeByte(nettyMessage.getHeader().getPriority());
        byteBuf.writeInt(nettyMessage.getHeader().getAttachment().size());

        String key;
        byte[] keyArr;
        for (Map.Entry<String, Object> entry : nettyMessage.getHeader().getAttachment().entrySet()) {
            key = entry.getKey();
            keyArr = key.getBytes("UTF-8");
            byteBuf.writeInt(key.length());
            byteBuf.writeBytes(keyArr);
            marshallingEncoder.encode(entry.getValue(), byteBuf);

        }
        key = null;
        keyArr = null;
        if (nettyMessage.getBody() != null) {
            marshallingEncoder.encode(nettyMessage.getBody(), byteBuf);
        } else {
            byteBuf.writeInt(0);
        }
        byteBuf.setInt(4, byteBuf.readableBytes());

        return byteBuf;
    }

    public NettyMessage decode(ByteBuf byteBuf) throws Exception {
        NettyMessage nettyMessage = new NettyMessage();
        Header header = new Header();

        header.setCrcCode(byteBuf.readInt());
        header.setLength(byteBuf.readInt());
        header.setSessionId(byteBuf.readLong());
        header.setType(byteBuf.readByte());
        header.setPriority(byteBuf.readByte());

        int objectSize = byteBuf.readInt();
        if (objectSize > 0) {
            Map<String, Object> attachment = Maps.newHashMapWithExpectedSize(objectSize);
            String key;
            byte[] keyArr;
            for (int i = 0; i < objectSize; i++) {
                keyArr = new byte[byteBuf.readInt()];
                byteBuf.readBytes(keyArr);
                key = new String(keyArr, "UTF-8");
                attachment.put(key, marshallingDecoder.decode(byteBuf));
            }

            header.setAttachment(attachment);
        }
        nettyMessage.setHeader(header);
        if (byteBuf.readableBytes() > 4) {
            nettyMessage.setBody(marshallingDecoder.decode(byteBuf));
        }

        return nettyMessage;
    }

    public static void main(String[] args) {
        try {
            CodecTest codecTest = new CodecTest();
            NettyMessage message = codecTest.buildMessage();

            System.out.println(" before encode : " + message.toString());
            ByteBuf byteBuf = codecTest.encode(message);
            message = codecTest.decode(byteBuf);

            System.out.println(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
