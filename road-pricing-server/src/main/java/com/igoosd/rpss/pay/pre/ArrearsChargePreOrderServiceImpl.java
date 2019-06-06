package com.igoosd.rpss.pay.pre;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PaymentStatusEnum;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsPreOrderService;
import com.igoosd.rpss.vo.BusinessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/3/1.
 */
@Service
public class ArrearsChargePreOrderServiceImpl extends AbsPreOrderService {

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;

    @Override
    public OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.ARREARS_CHARGE_ORDER;
    }


    @Override
    protected List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo) {
        List<TArrearsRecord> list = arrearsRecordMapper.selectList(new EntityWrapper<TArrearsRecord>().in("id", businessInfo.getArrearsRecordIds()).eq("payment_status", PaymentStatusEnum.UNPAID.getValue()));
        List<TOrderItem> items = new ArrayList<>(list.size());
        for (TArrearsRecord record : list) {
            TOrderItem item = new TOrderItem();
            item.setBizType(OrderBizTypeEnum.ARREARS_RECHARGE.getValue());
            item.setBizId(record.getId());
            item.setPayAmount(record.getArrearsAmount());
            items.add(item);
        }
        return items;
    }


}
