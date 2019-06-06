package com.igoosd.rpss.charge;

import com.igoosd.common.enums.ChargeRuleTypeEnum;
import com.igoosd.common.util.Const;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 2018/2/26.
 */
@Service
@Order(1)
public class TimeAreaChargeService extends ChargeService {

    @Override
    public ChargeRuleTypeEnum getChargeRuleTypeEnum() {
        return ChargeRuleTypeEnum.TIME_AREA;
    }

    @Override
    public BigDecimal getChargeAmount(Long milliSe, TreeMap<Object, Object> chargeTreeMap) {
        Long days = TimeUnit.MILLISECONDS.toDays(milliSe);
        Long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(milliSe) - Const.DAY_MINUTES * days;
        return getDiffTimeDaysCharge(chargeTreeMap, days).add(getDiffTimeMinutesCharge(chargeTreeMap, diffMinutes));
    }
}
