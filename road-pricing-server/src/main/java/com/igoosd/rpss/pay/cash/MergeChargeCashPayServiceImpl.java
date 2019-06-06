package com.igoosd.rpss.pay.cash;

import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsCashPayService;
import com.igoosd.rpss.vo.BusinessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/3/1.
 * 聚合缴费预下单接口
 */
@Service
public class MergeChargeCashPayServiceImpl extends AbsCashPayService {


    @Autowired
    private ArrearsChargeCashPayServiceImpl arrearsChargePreOrderService;

    @Autowired
    private ParkingChargeCashPayServiceImpl parkingChargePreOrderService;


    @Override
    public OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.MERGE_CHARGE_ORDER;
    }

    @Override
    protected List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo) {
        try {
            //欠费订单增强
            List<TOrderItem> arrearsItems = arrearsChargePreOrderService.getOrderItemsForBiz(businessInfo);
            //停车缴费增强
            List<TOrderItem> parkingItems = parkingChargePreOrderService.getOrderItemsForBiz(businessInfo);
            arrearsItems.addAll(parkingItems);
            return arrearsItems;
        } catch (RoadPricingException e) {
            throw new RoadPricingException("现金支付" + getOrderTypeEnum().getName() + "异常；明细如下：" + e.getMessage());
        }
    }

    @Override
    protected void postDoBiz(BusinessInfo businessInfo) {
        arrearsChargePreOrderService.postDoBiz(businessInfo);
        parkingChargePreOrderService.postDoBiz(businessInfo);
    }


}
