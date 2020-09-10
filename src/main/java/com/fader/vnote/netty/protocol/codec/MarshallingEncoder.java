package com.fader.vnote.netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;

/**
 * @author FaderW
 * 2019/8/27
 */

public class MarshallingEncoder {

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private Marshaller marshaller;

    public MarshallingEncoder() throws IOException {
        marshaller = MarshallingCodecFactory.buildMarshaller();
    }

    public void encode(Object msg, ByteBuf byteBuf) throws Exception {
        int lengthPos = byteBuf.writerIndex();
        byteBuf.writeBytes(LENGTH_PLACEHOLDER);
        ChannelBufferByteOutput output = new ChannelBufferByteOutput(byteBuf);

        try {
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();

            byteBuf.setInt(lengthPos, byteBuf.writerIndex() - lengthPos - 4);
        } finally {
            marshaller.close();
        }

    }
}
