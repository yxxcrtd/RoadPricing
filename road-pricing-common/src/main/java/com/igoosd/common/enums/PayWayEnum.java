package com.igoosd.common.enums;

/**
 * 2018/3/1.
 * 支付方式
 */
public enum PayWayEnum {

    WECHAT_PAY(1, "微信"), ALI_PAY(2, "支付宝"), BANK_CARD_PAY(3, "银行卡"), CASH_PAY(4, "现金");

    private int value;
    private String name;

    PayWayEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }


    public static PayWayEnum getPayWayEnum(int value) {
        for (PayWayEnum e : PayWayEnum.values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
