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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.igoosd.model.TVehicleEntranceRecord;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @see
 * @author 	hurd@igoosd.com
 * @date	2018-02-02 15:46:37 中国标准时间
 * @version	V0.0.1
 * @desc    TODO
 */
@Data
@ToString(callSuper = true)
public class VehicleEntranceRecord extends TVehicleEntranceRecord {


    private String parkingName;
    private String parkingAddress;
    private String memberTypeName;
    private String parkingSpaceCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date memberStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date memberEndDate;


    private Date startTime;

    private Date endTime;


    private String enterCollectorName;
    private String exitCollectorName;

}

