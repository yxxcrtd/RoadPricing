package com.igoosd.rpss.charge;

import com.igoosd.common.enums.ChargeRuleTypeEnum;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2018/2/26.
 */
public abstract class ChargeService {

    public abstract ChargeRuleTypeEnum getChargeRuleTypeEnum();

    /**
     *
     * @param milliSe 时间差  毫秒单位
     * @param chargeTreeMap 收费规则 treeMap
     */
    public abstract BigDecimal getChargeAmount(Long milliSe, TreeMap<Object,Object> chargeTreeMap);

    /**
     * 周期内剩余分钟数 计费Object
     * @param chargeTreeMap
     * @param diffMinutes
     * @return
     */
    protected BigDecimal getDiffTimeMinutesCharge(TreeMap<Object,Object> chargeTreeMap,Long diffMinutes){
        if(diffMinutes > 0){
            for (Map.Entry<Object,Object> entry : chargeTreeMap.entrySet()){
                if(Long.valueOf(entry.getKey().toString()) >= diffMinutes){
                    return new BigDecimal(entry.getValue().toString());
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 几日内最高收费计算
     * @param chargeTreeMap
     * @param diffDays
     * @return
     */
    protected BigDecimal getDiffTimeDaysCharge(TreeMap<Object,Object> chargeTreeMap,Long diffDays){
        Map.Entry<Object,Object> maxEntry =chargeTreeMap.lastEntry();
        return new BigDecimal(maxEntry.getValue().toString()).multiply(new BigDecimal(diffDays));
    }

}
