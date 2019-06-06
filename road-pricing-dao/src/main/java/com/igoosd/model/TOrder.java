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
@TableName("t_order")
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 订单类型 1：缴费订单 2：欠费补缴订单 3：聚合订单 4：扎帐订单
     */
    private Integer orderType;
    /**
     * 收费员ID
     */
    private Long collectorId;
    private String orderNo;
    /**
     * 支付方式 1：微信 2支付宝 3银行卡 4：现金
     */
    private Integer payWay;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付状态 1：待支付 2：支付成功 3：支付失败  4：支付取消 5:支付处理中 6：支付超时 7：交易异常
     */
    private Integer payStatus;
    /**
     * 预支付交易对象响应信息
     */
    private String chargeRspInfo;
    /**
     * 支付结果回调信息
     */
    private String callbackInfo;
    private String remark;
    private Date createTime;
    private Date updateTime;

}
