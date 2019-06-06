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
@TableName("t_location_report")
public class TLocationReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 收费员ID
     */
    private Long collectorId;
    private String location;
    /**
     * 上报时间 yyyy-MM-dd HH:mm:ss
     */
    private Date reportTime;
    /**
     * 是否在允许的考勤范围内
     */
    private Boolean allow;

    private Integer distance;


}
