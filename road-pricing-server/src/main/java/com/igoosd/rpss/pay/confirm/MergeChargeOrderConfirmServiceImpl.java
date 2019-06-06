package com.igoosd.rpss.pay.confirm;

import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsOrderConfirmService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 2018/3/5.
 */
@Service
@Order(2)
public class MergeChargeOrderConfirmServiceImpl extends AbsOrderConfirmService {

    @Autowired
    private ArrearsChargeOrderConfirmServiceImpl arrearsChargeOrderConfirmService;
    @Autowired
    private ParkingChargeOrderConfirmServiceImpl parkingChargeOrderConfirmService;

    @Override
    public int getOrderType() {
        return OrderTypeEnum.MERGE_CHARGE_ORDER.getValue();
    }

    @Override
    protected void confirmBiz(boolean isSuccess,  TOrderItem... items) {
        TOrderItem[] parkingItems = new TOrderItem[1];
        TOrderItem[] arrearsItems = null;

        for (int i=0; i< items.length;i++){
            if(items[i].getBizType().equals(OrderBizTypeEnum.PARKING_CHARGE.getValue())){
                parkingItems[0] = items[i];
                arrearsItems = ArrayUtils.remove(items,i);
                break;
            }
        }
        arrearsChargeOrderConfirmService.confirmBiz(isSuccess,arrearsItems);
        parkingChargeOrderConfirmService.confirmBiz(isSuccess,parkingItems);
    }
}
