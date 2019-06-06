package com.igoosd.common.enums;

/**
 * 2018/3/2.
 * 扎帐状态
 * 1 待扎帐初始状态                                0000
 * 1 线下扎帐成功 --收费员扎帐成功                  0001
 * 2.线上扎帐成功 -- 半夜任务调度 扎帐成功          0010
 * 3. 扎帐成功 --当日以收费员分隔 确定收费完成情况  0011
 */
public enum SummaryStatusEnum {

    SUMMARY_WAITTING(0, "待扎帐"), OFF_SUMMARY_SUCCESS(1, "线下扎帐成功"),ONLINE_SUMMARY_SUCCESS(2,"线上扎帐成功"),SUMMARY_SUCCESS(3,"扎帐成功");


    private int value;
    private String name;

    SummaryStatusEnum(int value, String name) {
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
