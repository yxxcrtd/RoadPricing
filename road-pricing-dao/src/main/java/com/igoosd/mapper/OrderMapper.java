package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.Order;
import com.igoosd.model.TOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface OrderMapper extends BaseMapper<TOrder> {


    /**
     * 根据条件查询总订单金额
     * @param order
     * @return
     */
    BigDecimal getOrderTotalAmount(Order order);


    /**
     * 查询不确定的订单状态
     * @param collector
     * @param startDate
     * @param endDate
     * @return
     */
    List<Order> queryUncertaintyOrderList(Long collector, Date startDate, Date endDate);

    /**
     * 根据支付方式获取支付成功的订单总额
     * @param startDate
     * @param endDate
     * @param payWayList
     * @return
     */
    BigDecimal getSumPayWayPayAmount(@Param("startDate")Date startDate, @Param("endDate") Date endDate, @Param("list") List<Integer> payWayList);

    /**
     *
     * 分页查询列表
     * @param rb
     * @param order
     * @return
     */
    List<Order> fuzzyFindPage(RowBounds rb, Order order);
}
