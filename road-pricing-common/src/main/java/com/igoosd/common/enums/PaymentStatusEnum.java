package com.igoosd.common.enums;

/**
 * 2018/2/27.
 * 欠费订单枚举
 */
public enum PaymentStatusEnum {

    UNPAID(0),PAID(1),;


    private int value;

    PaymentStatusEnum(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
