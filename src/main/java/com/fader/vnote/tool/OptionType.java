package com.fader.vnote.tool;

/**
 * 选项类型
 */
public enum OptionType {

    RADIO(0, "单选框"),
    CHECKBOX(1, "复选框"),
    NUMBER(2, "数字"),
    TEXT(3, "文本"),
    SELECT(4, "下拉框"),
    ;

    private int code;

    private String desc;

    OptionType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
