package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 配置项管理
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_sys_config")
public class TSysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String cfgKey;
    /**
     * 配置项值
     */
    private String cfgValue;
    /**
     * 描述
     */
    private String remark;


}
