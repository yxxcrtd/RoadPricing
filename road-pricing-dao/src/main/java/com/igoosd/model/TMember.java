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
@TableName("t_member")
public class TMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 会员车牌号
     */
    private String carNumber;
    /**
     * 免费路段ID  0 表示所有路段均可
     */
    private Long subParkingId;
    /**
     * 会员类型ID
     */
    private Long memberTypeId;
    /**
     * 会员开始日期 yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 会员截止日期 yyyy-MM-dd
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Date createTime;
    private String creator;
    /**
     * 入会金额 收费金额
     */
    private BigDecimal chargeAmount;

}
