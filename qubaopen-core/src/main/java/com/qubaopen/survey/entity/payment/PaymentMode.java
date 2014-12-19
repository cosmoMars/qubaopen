package com.qubaopen.survey.entity.payment;

/**
 * 支付方式枚举
 *
 */
public enum PaymentMode {

    /**
     * 支付宝
     */
    ALIPAY("1", "alipay", "支付宝"),

    /**
     * 微信
     */
    WXPAY("2", "wxpay", "微信"),

    /**
     * 快钱
     */
    KUAIQIAN("3", "kuaiqian", "快钱"),
    
    /** 联动U付. */
    UMP("4", "ump", "联动U付"),

    /** 快钱网关. */
    KUAIQIAN_RMBPORT("5", "kuaiqian_rmbport", "快钱网关");

    /**
     * 支付方式内部代码
     */
    private String code;

    /**
     * 支付方式KEY
     */
    private String key;

    /**
     * 支付方式名称
     */
    private String name;

    PaymentMode(String code, String key, String name) {
        this.code = code;
        this.key = key;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public static PaymentMode parse(String code) {
        PaymentMode right = null; // Default
        for (PaymentMode item : PaymentMode.values()) {
            if (item.getCode().equals(code)) {
                right = item;
                break;
            }
        }
        return right;
    }
    
    public static PaymentMode parseByName(String name) {
    	PaymentMode right = null; // Default
        for (PaymentMode item : PaymentMode.values()) {
            if (item.getName().equals(name)) {
                right = item;
                break;
            }
        }
        return right;
    }

    public String toString() {
        return code;
    }
}
