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
@TableName("t_parking")
public class TParking implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    /**
     * 总停车位数
     */
    private Integer totalParkingSpace;
    private Date createTime;
    private String address;
    /**
     * 收费规则ID
     */
    private Long chargeRuleId;


}
