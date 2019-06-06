package com.igoosd.common.enums;

/**
 * 2018/2/27.
 */
public enum BizStatusEnum {

    CAR_ENTER(0,"车辆驶入"),EXIT_PAYING(1,"驶出支付中"), CAR_EXIT(2,"缴费驶出"), ARREARS_EXIT(3,"欠费离场"), ARREARS_CHARGE(4,"欠费补缴"), MANUAL_WIPE_ARREARS(5,"欠费人工抹账");


    private int value;

    private String name;


    BizStatusEnum(int value,String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
