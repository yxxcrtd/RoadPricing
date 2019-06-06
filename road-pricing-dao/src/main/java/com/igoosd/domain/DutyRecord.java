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

import com.igoosd.model.TDutyRecord;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @see
 * @author 	hurd@igoosd.com
 * @date	2018-02-02 15:46:33 中国标准时间
 * @version	V0.0.1
 * @desc    TODO
 */
@Data
@ToString(callSuper = true)
public class DutyRecord extends TDutyRecord{


    private String subParkingName;

    private String collectorName;

    private Date startTime;

    private Date endTime;
}

