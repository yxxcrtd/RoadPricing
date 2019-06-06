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
@TableName("t_arrears_record")
public class TArrearsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 欠费金额
     */
    private BigDecimal arrearsAmount;
    /**
     * 停车记录ID
     */
    private Long vehicleEntranceId;
    /**
     * 欠费状态 0：欠费未支付  1：已补交
     */
    private Integer paymentStatus;
    private Date createTime;
    private Date updateTime;


}
