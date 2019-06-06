package com.igoosd.common.enums;

/**
 * 2018/3/15.
 */
public enum  OrderBizTypeEnum {

    PARKING_CHARGE(1,"停车缴费业务"),ARREARS_RECHARGE(2,"欠费补缴业务"),SUMMARY_ACCOUNT_CHARGE(3,"扎帐业务");


    private  int value;
    private String name;

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }



    OrderBizTypeEnum(int value,String name){
        this.value = value;
        this.name = name;
    }
}
