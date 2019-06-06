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
 * 
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_charge_rule")
public class TChargeRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    /**
     * 收费开始时间 HH:mm:ss
     */
    @JsonFormat(pattern = "hh:mm")
    private Date startTime;
    /**
     * 收费截止时间 HH:mm:ss
     */
    @JsonFormat(pattern = "hh:mm")
    private Date endTime;
    private String description;
    /**
     * 规则类型 1：分时段收费 2：24小时循环收费 3：24小时自然日收费
     */
    private Integer ruleType;
    private String ruleContent;
    private Date createTime;
    private BigDecimal maxChargeAmount;

}
