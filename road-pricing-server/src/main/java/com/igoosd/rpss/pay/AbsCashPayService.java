package com.igoosd.rpss.pay;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.Order;
import com.igoosd.mapper.OrderItemMapper;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.vo.BusinessInfo;
import com.igoosd.rpss.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 2018/3/23.
 */
@Slf4j
public abstract class AbsCashPayService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

    public  abstract OrderTypeEnum getOrderTypeEnum();
    /**
     * 获取订单明细列表 业务处理可在此处处理
     *
     * @param businessInfo
     * @return
     */
    protected abstract List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo);
    /**
     * 业务后置处理
     * @param businessInfo
     */
    protected abstract void postDoBiz(BusinessInfo businessInfo);


    /**
     * 现金支付 --只允许pos机 操作
     */
    @Transactional(rollbackFor = Exception.class)
    public Long  cashPay(BusinessInfo businessInfo){
        List<TOrderItem> items = getOrderItemsForBiz(businessInfo);
        BigDecimal totalAmount = getTotalAmount(items);
        Assert.isTrue(totalAmount.compareTo(BigDecimal.ZERO) > 0, "订单金额必须大于0");
        String orderNo = genOrderNo(PayWayEnum.CASH_PAY.getValue());

        Order order = new Order();
        order.setOrderType(getOrderTypeEnum().getValue());
        order.setCollectorId(LoginUser.get().getCollectorId());
        order.setPayAmount(getTotalAmount(items));
        order.setCreateTime(new Date());
        order.setPayWay(PayWayEnum.CASH_PAY.getValue());
        order.setPayStatus(PayStatusEnum.PAY_SUCCESS.getValue());//现金支付直接成功
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setOrderNo(orderNo);
        orderMapper.insert(order);
        for (TOrderItem item : items) {
            item.setOrderId(order.getId());
            item.setPayStatus(order.getPayStatus());
            item.setCreateTime(order.getCreateTime());
            item.setUpdateTime(order.getUpdateTime());
        }
        orderItemMapper.batchInsert(items);
        log.info("订单类型:{},现金支付完成！", getOrderTypeEnum().getName());
        postDoBiz(businessInfo);

        return order.getId();
    }


    private BigDecimal getTotalAmount(List<TOrderItem> list) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (TOrderItem item : list) {
            totalAmount = totalAmount.add(item.getPayAmount());
        }
        return totalAmount;
    }

    /**
     * @param payWay
     * @return
     */
    private String genOrderNo(int payWay) {
        String time = DateUtil.formatDetailDate(new Date());
        StringBuilder sb = new StringBuilder(time);
        sb.append(getOrderTypeEnum().getValue())
                .append(payWay);
        for (int i = 0; i < 8; i++) {
            Random random = new Random();
            sb.append(random.nextInt(10));
        }
        sb.append(LoginUser.get().getCollectorId());
        return sb.toString();
    }




}
