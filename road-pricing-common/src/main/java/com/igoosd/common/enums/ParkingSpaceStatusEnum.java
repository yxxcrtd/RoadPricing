package com.igoosd.common.enums;

/**
 * 2018/3/6.
 * 车位状态枚举
 */
public enum  ParkingSpaceStatusEnum {

    IDLE(0,"空闲中"),USING(1,"占用中"), FREE_EXIT_CONFIRM(2,"免费离场确认中"),;

    private int value;

    private String name;

    ParkingSpaceStatusEnum(int value, String name) {
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
