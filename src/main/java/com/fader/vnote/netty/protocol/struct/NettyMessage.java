package com.fader.vnote.netty.protocol.struct;

import lombok.Data;

/**
 * @author FaderW
 * 2019/8/27
 */
@Data
public class NettyMessage {

    private Header header;

    private Object body;
}
