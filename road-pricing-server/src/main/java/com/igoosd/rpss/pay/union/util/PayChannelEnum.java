package com.igoosd.rpss.pay.union.util;

import com.igoosd.common.enums.PayWayEnum;

/**
 * 2018/3/21.
 */
public enum PayChannelEnum {

    ALI_PAY("A01",PayWayEnum.ALI_PAY),WECHAT_PAY("W01",PayWayEnum.WECHAT_PAY);

    private PayWayEnum payWayEnum;

    private String value;


    PayChannelEnum(String value, PayWayEnum payWayEnum){
        this.value = value;
        this.payWayEnum = payWayEnum;
    }

    public PayWayEnum getPayWayEnum() {
        return payWayEnum;
    }

    public String getValue() {
        return value;
    }


    public static PayChannelEnum getUnionPayChannelEnum(PayWayEnum payWayEnum){
        for (PayChannelEnum e : PayChannelEnum.values()){
            if (e.getPayWayEnum().equals(payWayEnum)) {
                return e;
            }
        }
        return null;
    }
}
