package com.igoosd.rpss.service.impl;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.domain.Order;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.model.TOrder;
import com.igoosd.rpss.pay.AbsCashPayService;
import com.igoosd.rpss.pay.AbsOrderConfirmService;
import com.igoosd.rpss.pay.AbsPreOrderService;
import com.igoosd.rpss.service.OrderService;
import com.igoosd.rpss.vo.BusinessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2018/3/5.
 */
@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    List<AbsPreOrderService> preOrderServiceList;
    @Autowired
    private List<AbsCashPayService> cashPayServiceList;
    @Autowired
    private List<AbsOrderConfirmService> orderConfirmServiceList;


    @Override
    public Order preOrder(int orderType, int payWay, BusinessInfo businessInfo) {

        for (AbsPreOrderService preOrderService : preOrderServiceList) {
            if (preOrderService.getOrderTypeEnum().getValue() == orderType) {
                return preOrderService.doPreOrder(PayWayEnum.getPayWayEnum(payWay), businessInfo);
            }
        }
        throw new RoadPricingException("找不到特定订单类型的预下单接口");
    }

    @Override
    public PayStatusEnum getOrderPayStatus(Long id) {
        TOrder order = orderMapper.selectById(id);
        Assert.notNull(order, "找不到指定的订单");
        return PayStatusEnum.getPayStatusEnum(order.getPayStatus());
    }


    @Override
    public BigDecimal getOrderAmount(Order order) {
        return orderMapper.getOrderTotalAmount(order);
    }


    @Override
    public Long payCash(BusinessInfo businessInfo, OrderTypeEnum orderTypeEnum) {
        for (AbsCashPayService cashPayService : cashPayServiceList) {
            if (cashPayService.getOrderTypeEnum() == orderTypeEnum) {
                return cashPayService.cashPay(businessInfo);
            }
        }
        throw new RoadPricingException("没有合适的订单类型处理接口,订单类型:" + orderTypeEnum.getName());
    }

    @Override
    public <T> void orderConfirm(String orderNo, PayStatusEnum e, T thirdResult) {
        TOrder tmp = new TOrder();
        tmp.setOrderNo(orderNo);
        TOrder order = orderMapper.selectOne(tmp);
        Assert.notNull(order, "找不到指定的订单，订单编号:{}", orderNo);
        orderConfirm(order, e, thirdResult);
    }

    @Override
    public <T> void orderConfirm(TOrder order, PayStatusEnum pse, T thirdResult) {
        //支付确认
        for (AbsOrderConfirmService confirmService : orderConfirmServiceList) {
            if (confirmService.getOrderType() == order.getOrderType()) {
                confirmService.confirm(pse.getValue(), order, thirdResult);
                return;
            }
        }
        throw new RoadPricingException("支付确认异常,没有指定的订单类型的确认接口,回调信息:{}", JSON.toJSONString(thirdResult));
    }

}
