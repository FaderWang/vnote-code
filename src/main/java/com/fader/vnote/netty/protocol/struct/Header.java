package com.fader.vnote.netty.protocol.struct;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author FaderW
 * 2019/8/27
 */
@Getter
@Setter
@ToString
public class Header {

    private int crcCode = 0xabef0101;

    /**
     * 消息长度
     */
    private int length;

    /**
     * 会话Id
     */
    private long sessionId;

    /**
     * 消息类型
     */
    private byte type;

    /**
     * 消息权重
     */
    private byte priority;

    /**
     * 附件
     */
    private Map<String, Object> attachment = Maps.newHashMap();

}
