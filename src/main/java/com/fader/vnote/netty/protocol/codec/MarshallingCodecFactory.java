package com.fader.vnote.netty.protocol.codec;

import org.jboss.marshalling.*;

import java.io.IOException;

/**
 * @author FaderW
 * 2019/8/27
 */

public final class MarshallingCodecFactory {

    /**
     * build jboss marshaller
     * @return
     * @throws IOException
     */
    public static Marshaller buildMarshaller() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);

        return marshaller;
    }


    /**
     * 创建jboss unmarshaller
     * @return
     * @throws IOException
     */
    public static Unmarshaller buildUnmarshaller() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling
                .getProvidedMarshallerFactory("serial");
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);

        return unmarshaller;
    }


}
