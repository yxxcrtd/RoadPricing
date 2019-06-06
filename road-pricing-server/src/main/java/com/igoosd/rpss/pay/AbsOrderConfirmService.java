package com.igoosd.rpss.pay;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.domain.OrderItem;
import com.igoosd.mapper.OrderItemMapper;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.model.TOrder;
import com.igoosd.model.TOrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 2018/3/2.
 * 订单确认接口
 */
public abstract class AbsOrderConfirmService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 订单类型
     */
    public abstract int getOrderType();


    /**
     * 业务确认
     *
     * @param isSuccess
     * @param items
     */
    protected abstract void confirmBiz(boolean isSuccess, TOrderItem... items);

    @Transactional(rollbackFor = Exception.class)
    public<T> void confirm(int payStatus, TOrder order, T callbackMap) {
        List<TOrderItem> list = orderItemMapper.selectList(new EntityWrapper<TOrderItem>().eq("order_id", order.getId()));
        boolean isSuccess = PayStatusEnum.PAY_SUCCESS.getValue() == payStatus;
        //业务更新
        confirmBiz(isSuccess, list.toArray(new TOrderItem[list.size()]));
        //订单更新
        TOrder tmp = new TOrder();
        tmp.setId(order.getId());
        tmp.setUpdateTime(new Date());
        tmp.setPayStatus(payStatus);
        tmp.setCallbackInfo(JSON.toJSONString(callbackMap));
        orderMapper.updateById(tmp);
        OrderItem item = new OrderItem();
        item.setUpdateTime(new Date());
        item.setPayStatus(payStatus);
        orderItemMapper.update(item, new EntityWrapper<TOrderItem>().eq("order_id", order.getId()));
    }
}
