package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
@TableName("t_device_pos")
public class TDevicePos implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String deviceId;
    private Long subParkingId;
    private String remark;
    private Integer deviceType;//设备类型 0 默认类型 1：P990 pos机
    private String bluetoothId;
}
