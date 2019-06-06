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
@TableName("t_collector")
public class TCollector implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Boolean sex;
    private String phone;
    private String address;
    /**
     * 工作状态 0：离职 1：在职
     */
    private Integer jobStatus;
    private Date createTime;
    /**
     * 工号
     */
    private String jobNumber;
    private String password;


}
