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

import com.igoosd.model.TMember;
import lombok.Data;
import lombok.ToString;

/**
 * @see
 * @author 	hurd@igoosd.com
 * @date	2018-02-02 15:46:34 中国标准时间
 * @version	V0.0.1
 * @desc    TODO
 */
@Data
@ToString(callSuper = true)
public class Member extends TMember{


    /**
     * 会员身份标志名称
     */
    private String memberTypeName;

    private String subParkingName;

}

