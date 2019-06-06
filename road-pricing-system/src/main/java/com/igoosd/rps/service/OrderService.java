package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Order;
import com.igoosd.model.TOrder;
import com.igoosd.rps.util.CommonService;

import java.math.BigDecimal;

/**
 * 2018/5/11.
 */
public interface OrderService extends CommonService<TOrder,Long> {

    Page<Order> fuzzyFindPage(Order order,Page<Order> page);


    /**
     * 获取总收入
     * @param isOnline
     * @return
     */
    BigDecimal getTotalAmountForCurDay( boolean isOnline);
}
