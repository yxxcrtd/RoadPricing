package com.igoosd.common.enums;

/**
 * 2018/2/26.
 */
public enum ChargeRuleTypeEnum {

    TIME_AREA(1, "分时段收费"), TIME_24HOURS_LOOP_DAY(2, "24小时循环日收费"), TIME_24HOURS_NATURAL_DAY(3, "24小时自然日收费");

    private Integer value;

    private String name;

    ChargeRuleTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public static ChargeRuleTypeEnum getChargeRuleTypeEnum(Integer value) {
        for (ChargeRuleTypeEnum cre : ChargeRuleTypeEnum.values()) {
            if (cre.getValue().equals(value)) {
                return cre;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
