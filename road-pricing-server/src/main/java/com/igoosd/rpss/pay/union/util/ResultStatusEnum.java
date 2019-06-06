package com.igoosd.rpss.pay.union.util;

import com.igoosd.common.enums.PayStatusEnum;

/**
 * 2018/3/22.
 * 交易结果码枚举
 */
public enum ResultStatusEnum {

    SUCCESS("0000", "交易成功", PayStatusEnum.PAY_SUCCESS),
    TIMEOUT_1("3045", "交易超时", PayStatusEnum.PAY_TIMEOUT),
    TIMEOUT_2("3088", "交易超时", PayStatusEnum.PAY_TIMEOUT),
    NO_BALANCE("3008", "余额不足", PayStatusEnum.PAY_FAIL),
    FAIL("3999", "交易失败", PayStatusEnum.PAY_FAIL),
    HANDING("2008", "交易处理中", PayStatusEnum.PAY_HANDING),
    CANCEL("3050", "交易已撤销", PayStatusEnum.PAY_FAIL);

    private String value;
    private String name;
    private PayStatusEnum payStatusEnum;


    ResultStatusEnum(String value, String name, PayStatusEnum payStatusEnum) {
        this.value = value;
        this.name = name;
        this.payStatusEnum = payStatusEnum;
    }

    public static final ResultStatusEnum getResultCodeEnum(String value) {
        for (ResultStatusEnum e : ResultStatusEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public PayStatusEnum getPayStatusEnum() {
        return payStatusEnum;
    }
}
