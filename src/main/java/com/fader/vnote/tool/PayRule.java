package com.fader.vnote.tool;

/**
 * 付款规则菜单
 */
public enum  PayRule {


    GLOBAL_CONFIG(-1, "付款控制"),
    SECURITY_OPTIONS(0, "安全选项"),
    AUTO_RULES(1, "自动规则"),
    BALANCE_CONTROL(2, "余额控制"),
    SUSPECTED_REPEAT_PAYMENT(3, "疑似重复打款"),
    ;

    private int code;

    private String desc;

    PayRule(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
