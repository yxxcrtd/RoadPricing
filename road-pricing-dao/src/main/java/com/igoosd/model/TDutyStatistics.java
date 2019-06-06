package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("t_duty_statistics")
public class TDutyStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long collectorId;
    private Long subParkingId;
    /**
     * 执勤日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dutyDate;
    /**
     * 最近签到（登入）时间
     */
    private Date latestOnDutyTime;
    /**
     * 最近签退（登出）时间
     */
    private Date latestOffDutyTime;
    /**
     * 在线总共时长 单位小时
     */
    private Double onlineTotalTime;
    private Date createTime;


}
