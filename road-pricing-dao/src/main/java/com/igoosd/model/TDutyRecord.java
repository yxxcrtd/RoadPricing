package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
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
@TableName("t_duty_record")
public class TDutyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long collectorId;
    private Long subParkingId;
    private Date loginTime;
    private String location;
    private String deviceId;
    private Long ip;
    /**
     * 登出时间,如果没有记录默认收费规则下班时间计算
     */
    private Date logoutTime;

}
