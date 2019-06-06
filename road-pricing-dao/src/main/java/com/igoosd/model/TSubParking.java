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
@TableName("t_sub_parking")
public class TSubParking implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 所属停车场ID
     */
    private Long parkingId;
    private String name;
    /**
     * 路段编号
     */
    private String code;
    /**
     * 路段地理左边 纬度,经度  
     */
    private String geoLocation;
    private Date createTime;

}
