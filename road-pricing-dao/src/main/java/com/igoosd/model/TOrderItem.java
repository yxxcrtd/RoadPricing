package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_order_item")
public class TOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 业务id
     */
    private Long bizId;
    /**
     * 业务类型： 1.停车缴费订单业务 2：欠费补缴业务 3 扎帐业务
     */
    private Integer bizType;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付状态
     */
    private Integer payStatus;
    private Date updateTime;
    private Date createTime;


}
