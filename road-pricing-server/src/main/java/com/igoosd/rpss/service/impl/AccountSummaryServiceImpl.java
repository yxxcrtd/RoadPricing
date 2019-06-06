package com.igoosd.rpss.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.Order;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.rpss.service.AccountSummaryService;
import com.igoosd.rpss.service.OrderService;
import com.igoosd.rpss.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 2018/3/2.
 */
@Service
public class AccountSummaryServiceImpl implements AccountSummaryService {

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;
    @Autowired
    private OrderService orderService;

    @Override
    public boolean allowLoginForAccountSummary(Long collectorId) {

        Object[] objs = new Object[2];
        objs[0] = SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue();
        objs[1] = SummaryStatusEnum.SUMMARY_SUCCESS.getValue();
        int count = accountSummaryMapper.selectCount(new EntityWrapper<TAccountSummary>()
                .eq("collector_id", collectorId)
                .eq("summary_date", DateUtil.convertYmdDate(new Date()))
                .in("summary_status", objs));
        return count == 0;
    }

    @Override
    public TAccountSummary getLatestAccountSummaryRecordByCollectorId(Long collectorId) {
        Date ymdDate = DateUtil.convertYmdDate(new Date());
        Order order = new Order();
        order.setStartTime(ymdDate);
        order.setEndTime(DateUtil.getNextDate(order.getStartTime()));
        order.setPayStatus(PayStatusEnum.PAY_SUCCESS.getValue());
        order.setCollectorId(LoginUser.get().getCollectorId());
        order.setPayWay(PayWayEnum.CASH_PAY.getValue());
        BigDecimal offlineAmount = orderService.getOrderAmount(order) != null ? orderService.getOrderAmount(order) : BigDecimal.ZERO;
        TAccountSummary temp = new TAccountSummary();
        temp.setCollectorId(collectorId);
        temp.setSummaryDate(ymdDate);
        TAccountSummary tAccountSummary = accountSummaryMapper.selectOne(temp);
        if (null != tAccountSummary) {
            if (tAccountSummary.getOfflineAmount().compareTo(offlineAmount) != 0
                    && (tAccountSummary.getSummaryStatus() & SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue()) != SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue()) {
                temp = new TAccountSummary();
                temp.setId(tAccountSummary.getId());
                temp.setOfflineAmount(offlineAmount);
                temp.setSummaryTime(new Date());
                accountSummaryMapper.updateById(temp);
                tAccountSummary.setOfflineAmount(offlineAmount);
            }
        } else {
            tAccountSummary = new TAccountSummary();
            tAccountSummary.setOfflineAmount(offlineAmount);
            tAccountSummary.setSummaryDate(ymdDate);
            tAccountSummary.setSummaryTime(new Date());
            tAccountSummary.setCollectorId(collectorId);
            tAccountSummary.setCreateTime(new Date());
            tAccountSummary.setSummaryStatus(SummaryStatusEnum.SUMMARY_WAITTING.getValue());
            accountSummaryMapper.insert(tAccountSummary);
        }

        return tAccountSummary;
    }

}
