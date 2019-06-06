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
@TableName("t_parking_space")
public class TParkingSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 车位号（车位编码）
     */
    private String code;
    /**
     * 地磁编码 备用
     */
    private String geoSensorCode;
    /**
     * 所属路段ID
     */
    private Long subParkingId;
    /**
     * 所属停车场ID 冗余
     */
    private Long parkingId;
    private Date createTime;
    /**
     * 乐观锁 标识
     */
    private Long versionNo;

}
