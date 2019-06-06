package com.igoosd.rpss.pay.pre;

import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.mapper.CollectorMapper;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.model.TCollector;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsPreOrderService;
import com.igoosd.rpss.vo.BusinessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/1.
 * 扎帐订单处理
 */
@Service
public class PitchAccountChargePreOrderServiceImpl extends AbsPreOrderService{


    @Autowired
    private AccountSummaryMapper accountSummaryMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CollectorMapper collectorMapper;

    @Override
    public OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.PITCH_ACCOUNT_CHARGE_ORDER;
    }

    @Override
    protected List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo) {
        TAccountSummary tas = accountSummaryMapper.selectById(businessInfo.getAccountSummaryId());
        List<TOrderItem> list = new ArrayList<>(1);
        TOrderItem item = new TOrderItem();
        item.setBizType(OrderBizTypeEnum.SUMMARY_ACCOUNT_CHARGE.getValue());
        item.setCreateTime(new Date());
        item.setPayAmount(tas.getOfflineAmount());
        item.setBizId(tas.getId());
        list.add(item);
        return list;
    }


}
