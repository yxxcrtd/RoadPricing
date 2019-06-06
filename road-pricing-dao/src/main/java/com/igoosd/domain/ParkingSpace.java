/**
 * @单位名称：安徽谷声科技有限公司
 *            IGOOSD Technology Co.,Ltd.
 * 		      Copyright (c) 2017 All Rights Reserved.
 * @系统名称：路泊收费系统
 * @工程名称：后台服务
 * @文件名称: 
 * @类路径: 
 */

package com.igoosd.domain;

import com.igoosd.model.TParkingSpace;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @see
 * @author 	hurd@igoosd.com
 * @date	2018-02-02 15:46:35 中国标准时间
 * @version	V0.0.1
 * @desc    TODO
 */
@Data
@ToString(callSuper = true)
public class ParkingSpace extends TParkingSpace{


    private String parkingName;
    private String subParkingCode;
    private String subParkingName;

    /**
     * 当前车位泊车 车牌
     */
    private String carNumber;

    /**
     * 出入场记录ID
     */
    private Long vehicleEntranceRecordId;

    /**
     * 会员类型ID
     */
    private Long memberTypeId;

    /**
     * 会员类型名称
     */
    private String memberTypeName;
    /**
     * 会员开始时间
     */
    private Date memberStartDate;

    /**
     * 会员结束时间
     */
    private Date memberEndDate;

    private BigDecimal totalArrearsAmount; //欠费金额

    private Date enterTime;//入场时间

    private int status =0;//车位状态  0：空闲 1：占用 2：免费离场确认中



    //批量新增字段
    private String splitChar;//车位编码分隔符号 可不填
    private Integer startNum;//开始序号
    private Integer endNum;//截止序号  截止序号>=开始序号

}

