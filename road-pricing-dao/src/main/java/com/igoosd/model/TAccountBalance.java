package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 对账记录表
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_account_balance")
public class TAccountBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 对账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balanceDate;
    /**
     * 应收总收入
     */
    private BigDecimal totalChargeAmount;
    /**
     * 会员优惠金额总数
     */
    private BigDecimal totalMemberPreferentialAmount;
    private BigDecimal totalRealChargeAmount;
    /**
     * 线上支付总收入
     */
    private BigDecimal totalOnlineAmount;
    /**
     * 线下现金总收入
     */
    private BigDecimal totalOfflineAmount;
    /**
     * 欠费补缴总收入
     */
    private BigDecimal totalArrearsChargeAmount;
    /**
     * 创建时间
     */
    private Date createTime;


}
