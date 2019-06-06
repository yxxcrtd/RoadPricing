package com.igoosd.rpss.pay;

import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 2018/3/21.
 * 通用支付接口
 */
public interface PayService {


    /**
     * 获取交易对象
     *
     * @param orderNo
     * @return 返回交易对象 Map
     */
    Map<String, String> pay(String orderNo, PayWayEnum payWayEnum, BigDecimal realChargeAmount, OrderTypeEnum orderTypeEnum);


    /**
     * 处理回调业务信息
     */
    void doCallbackBiz(Map<String, String> rstMap);

    /**
     * 交易查询
     *
     * @return
     */
    Map<String, String> queryCharge(Map<String, String> chargeInfoMap);

    /**
     * 交易查询
     *
     * @param orderNo
     * @return
     */
    Result queryCharge(String orderNo);


    @Data
    final class Result<T> {
        private PayStatusEnum payStatusEnum;
        private T queryResult;

        public Result(PayStatusEnum payStatusEnum,T queryResult){
            this.payStatusEnum = payStatusEnum;
            this.queryResult =queryResult;
        }
    }
}
