package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.Order;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.model.TOrder;
import com.igoosd.rps.service.OrderService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/5/11.
 */
@Service
public class OrderServiceImpl extends AbsCommonService<TOrder, Long> implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    protected BaseMapper<TOrder> getMapper() {
        return orderMapper;
    }

    @Override
    public Page<Order> fuzzyFindPage(Order order, Page<Order> page) {
        List<Order> list = orderMapper.fuzzyFindPage(page, order);

        return page.setRecords(list);
    }

    @Override
    public BigDecimal getTotalAmountForCurDay(boolean isOnline) {

        Date startDate = DateUtil.convertYmdDate(new Date());
        Date endDate = DateUtil.getNextDate(startDate);

        List<Integer> list = new ArrayList<>();
        if (isOnline) {
            list.add(PayWayEnum.WECHAT_PAY.getValue());
            list.add(PayWayEnum.ALI_PAY.getValue());
        } else {
            list.add(PayWayEnum.CASH_PAY.getValue());
        }

        return orderMapper.getSumPayWayPayAmount(startDate, endDate, list);
    }
}
