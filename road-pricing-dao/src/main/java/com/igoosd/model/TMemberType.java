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
@TableName("t_member_type")
public class TMemberType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    /**
     * 会员类型时间 : 单位 月 
     */
    private Integer validTime;
    private String remark;
    private Date createTime;

}
