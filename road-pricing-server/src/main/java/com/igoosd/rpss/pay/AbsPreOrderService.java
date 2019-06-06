package com.igoosd.rpss.pay;

import com.alibaba.fastjson.JSON;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 2018/2/28.
 */
@Slf4j
public abstract class AbsPreOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private PayService payService;


    public abstract OrderTypeEnum getOrderTypeEnum();


    /**
     * 获取订单 收费员ID 0 标识系统
     *
     * @return
     */
    public Long getCollectorId() {
        LoginUser user = LoginUser.get();
        return user == null ? 0L : user.getCollectorId();
    }

    /**
     * 获取订单明细列表 业务处理可在此处处理
     *
     * @param businessInfo
     * @return
     */
    protected abstract List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo);


    @Transactional(rollbackFor = Exception.class)
    public Order doPreOrder(PayWayEnum payWay, BusinessInfo businessInfo) {
        List<TOrderItem> items = getOrderItemsForBiz(businessInfo);
        BigDecimal totalAmount = getTotalAmount(items);
        Assert.isTrue(totalAmount.compareTo(BigDecimal.ZERO) > 0, "订单金额必须大于0");
        String orderNo = genOrderNo(payWay.getValue());
        //申请支付交易对象
        Map<String, String> rstMap = payService.pay(orderNo, payWay, totalAmount, getOrderTypeEnum());
        Order order = new Order();
        order.setOrderType(getOrderTypeEnum().getValue());
        order.setCollectorId(getCollectorId());
        order.setPayAmount(getTotalAmount(items));
        order.setCreateTime(new Date());
        order.setPayWay(payWay.getValue());
        order.setPayStatus(PayStatusEnum.PRE_PAYING.getValue());
        order.setCreateTime(new Date());
        order.setOrderNo(orderNo);
        order.setChargeRspInfo(JSON.toJSONString(rstMap));
        orderMapper.insert(order);
        for (TOrderItem item : items) {
            item.setOrderId(order.getId());
            item.setPayStatus(order.getPayStatus());
            item.setCreateTime(order.getCreateTime());
        }
        orderItemMapper.batchInsert(items);
        log.info("{}预下单完成！", getOrderTypeEnum().getName());
        //将qrUrl 放入order
        order.setQrCodeUrl(rstMap.get("payinfo"));
        return order;
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
        sb.append(getCollectorId());
        return sb.toString();
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        System.out.println(sdf.format(new Date()));
    }

}
