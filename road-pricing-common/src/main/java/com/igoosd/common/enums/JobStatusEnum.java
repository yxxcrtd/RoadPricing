package com.igoosd.common.enums;

/**
 * 2018/2/5.
 */
public enum  JobStatusEnum {

    QUIT_JOB(0,"离职"),On_JOB(1,"在职");

    private int value;
    private String name;

    JobStatusEnum(int value,String name){
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
