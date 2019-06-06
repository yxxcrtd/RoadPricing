package com.igoosd.common.enums;

/**
 * 2018/2/28.
 * 订单类型枚举 1：缴费订单  2：欠费补缴订单 3：聚合订单 4：扎帐订单
 */
public enum OrderTypeEnum {

    PARKING_CHARGE_ORDER(1, "缴费订单"),
    ARREARS_CHARGE_ORDER(2, "欠费补缴订单"),
    MERGE_CHARGE_ORDER(3, "聚合订单"),
    PITCH_ACCOUNT_CHARGE_ORDER(4, "扎帐订单");


    private int value;
    private String name;


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    OrderTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static OrderTypeEnum getOrderTypeEnum(int value) {
        for (OrderTypeEnum e : OrderTypeEnum.values()) {
            if (e.getValue() == value) {
                return e;
            }
        }
        return null;
    }


}
