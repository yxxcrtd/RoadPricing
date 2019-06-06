package com.igoosd.rpss.service;

import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.domain.Order;
import com.igoosd.model.TOrder;
import com.igoosd.rpss.vo.BusinessInfo;

import java.math.BigDecimal;

/**
 * 2018/3/5.
 */
public interface OrderService {


    /**
     * 预下单
     *
     * @param orderType
     * @param payWay
     * @return 返回支付二维码url
     */
    Order preOrder(int orderType, int payWay, BusinessInfo businessInfo);


    /**
     * 根据主键获取订单
     */
    PayStatusEnum getOrderPayStatus(Long id);

    /**
     * 获取订单总数
     *
     * @param order
     * @return
     */
    BigDecimal getOrderAmount(Order order);

    /**
     * 现金支付
     *
     * @param businessInfo
     * @param orderTypeEnum
     * @return  返回订单ID
     */
    Long payCash(BusinessInfo businessInfo, OrderTypeEnum orderTypeEnum);


    /**
     * 后台订单确认接口
     */
    <T> void orderConfirm(String orderNo, PayStatusEnum pse, T result);

    <T> void orderConfirm(TOrder order, PayStatusEnum pse, T result);
}
