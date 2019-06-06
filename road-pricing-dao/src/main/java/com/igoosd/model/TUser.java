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
@TableName("t_user")
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 用户类型 ：1:管理员 2：财务人员...
     */
    private Integer userType;
    /**
     * 登录密码
     */
    private String password;
    private Boolean sex;
    private String phone;
    private String salt;
    private Date createTime;
    private Date updateTime;

    private Boolean delete;


}
