package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 出入场记录与图片关联表
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_ver_picture")
public class TVerPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 出入场记录ID
     */
    private Long vehicleEntranceRecordId;
    /**
     * 图片URL地址 相对地址
     */
    private String picturlUrl;
    /**
     * 图片上传顺序
     */
    private Integer orderNum;


}
