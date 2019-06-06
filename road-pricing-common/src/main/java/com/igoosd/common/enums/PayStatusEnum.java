package com.igoosd.common.enums;

/**
 * 2018/3/1.
 */
public enum  PayStatusEnum {

    PRE_PAYING(1,"待支付"),PAY_SUCCESS(2,"支付成功"),PAY_FAIL(3,"支付失败"),PAY_CANCEL(4,"支付取消"),PAY_HANDING(5,"支付处理中"),PAY_TIMEOUT(6,"支付超时"),PAY_EXCEPTION(7,"交易异常");

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    private int value;
    private String name;

    PayStatusEnum(int value,String name){
        this.value = value;
        this.name = name;
    }

    public static PayStatusEnum getPayStatusEnum(int value){
        for (PayStatusEnum e : PayStatusEnum.values()){
            if(e.getValue() == value){
                return e;
            }
        }
        return null;
    }
}
