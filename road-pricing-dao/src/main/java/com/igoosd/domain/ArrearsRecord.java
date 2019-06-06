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

import com.igoosd.model.TArrearsRecord;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @see
 * @author 	hurd@igoosd.com
 * @date	2018-02-02 15:46:32 中国标准时间
 * @version	V0.0.1
 * @desc    TODO
 */
@Data
@ToString(callSuper = true)
public class ArrearsRecord extends TArrearsRecord{
	

    private String parkingName; //停车场名称

    private Date enterTime;

    private Date exitTime;


    private  Date startTime; //分页查询时间范围内的欠费情况

    private Date endTime; //分页查询时间范围内的欠费情况

}

