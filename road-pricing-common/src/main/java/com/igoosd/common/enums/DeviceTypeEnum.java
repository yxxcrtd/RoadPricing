package com.igoosd.common.enums;

/**
 * 2018/5/10.
 */
public enum  DeviceTypeEnum {
    COMMON_PDA(0,"普通PDA"),P990_PDA(1,"P990 Pos机");

    private int value;
    private String name;


    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    DeviceTypeEnum(int value, String name){
        this.value = value;
        this.name =  name;
    }


    public static DeviceTypeEnum getDeviceTypeEnumByValue(Integer value){
        if(null != value){
            for (DeviceTypeEnum e : DeviceTypeEnum.values()){
                if(e.getValue() == value){
                    return e;
                }
            }
        }
        return null;
    }
}
