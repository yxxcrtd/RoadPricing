package com.igoosd.rpss.service;

import com.igoosd.model.TChargeRule;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2018/2/26.
 */
public interface ChargeRuleService {


    /**
     * 计算指定停车场 从驶入时间到驶出时间 的停车费用
     * @param parkingId
     * @param enterTime
     * @param exitTime
     * @return
     */
     BigDecimal getChargeAmount(Long parkingId, Date enterTime,Date exitTime);

    /**
     * 计算真实停车费用
     * @param parkingId
     * @param enterTime
     * @param exitTime
     * @param carNumber
     * @return
     */
     BigDecimal getRealChargeAmount(Long parkingId,Long subParkingId, Date enterTime,Date exitTime,String carNumber);


    /**
     * 获取停车场收费规则
     * @param parkingId
     * @return
     */
     TChargeRule  getChargeRuleByParkingId(Long parkingId);
}
