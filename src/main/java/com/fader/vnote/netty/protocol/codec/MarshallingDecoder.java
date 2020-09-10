package com.fader.vnote.netty.protocol.codec;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * @author FaderW
 * 2019/8/27
 */

public class MarshallingDecoder {

    private Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException {
        this.unmarshaller = MarshallingCodecFactory.buildUnmarshaller();
    }

    public Object decode(ByteBuf byteBuf) throws Exception {
        int objectSize = byteBuf.readInt();
        ByteBuf buf = byteBuf.slice(byteBuf.readerIndex(), objectSize);
        ChannelBufferByteInPut inPut = new ChannelBufferByteInPut(buf);

        try {
            unmarshaller.start(inPut);
            Object object = unmarshaller.readObject();
            unmarshaller.finish();

            byteBuf.readerIndex(byteBuf.readerIndex() + objectSize);
            return object;
        } finally {
            unmarshaller.close();
        }
    }
}
