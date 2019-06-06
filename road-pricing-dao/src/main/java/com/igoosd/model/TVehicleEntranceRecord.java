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
@TableName("t_vehicle_entrance_record")
public class TVehicleEntranceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 入场时间
     */
    private Date enterTime;
    private Long enterCollectorId;
    /**
     * 出场时间
     */
    private Date exitTime;
    private Long exitCollectorId;
    /**
     * 停车场Id
     */
    private Long parkingId;
    /**
     * 路段Id
     */
    private Long subParkingId;
    /**
     * 车位ID
     */
    private Long parkingSpaceId;
    /**
     * 车牌号
     */
    private String carNumber;
    private BigDecimal chargeAmount;
    private BigDecimal realChargeAmount;
    /**
     * 业务状态 0：车辆驶入 1：驶出支付中 2：缴费驶出 3：欠费离场 4：欠费补缴 5：人工干预
     */
    private Integer bizStatus;
    /**
     * 会员ID
     */
    private Long memberId;
    /**
     * 车位乐观锁标识快照
     */
    private Long versionNo;
    private Date createTime;
    private Date updateTime;

}
