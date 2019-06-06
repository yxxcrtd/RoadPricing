package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.OrderItem;
import com.igoosd.mapper.OrderItemMapper;
import com.igoosd.model.TOrderItem;
import com.igoosd.rps.service.OrderItemService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/5/11.
 */
@Service
public class OrderItemServiceImpl extends AbsCommonService<TOrderItem, Long> implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    protected BaseMapper<TOrderItem> getMapper() {
        return orderItemMapper;
    }

}
