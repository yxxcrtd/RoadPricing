package com.igoosd.rpss.vo;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

/**
 * 2018/2/5.
 */
@Slf4j
@Data
@ToString
public class LoginUser {

    private Long collectorId;
    private String collectorName;
    private String phone;
    private String jobNumber;

    private Long parkingId;
    private String parkingName;
    private Long subParkingId;

    private Date loginDate;

    private String parkingAddress;

    private List<Long> parkingSpaceIds;

    private Long dutyRecordId; //执勤记录id

    private String subParkingLocation;//路段地理坐标

    private String ip;

    private String deviceId;//设备ID

    private static final ThreadLocal<LoginUser> THREAD_LOCAL = new InheritableThreadLocal<>();


    public static final LoginUser get(){
        return THREAD_LOCAL.get();
    }

    public static final void  set(LoginUser user){
        THREAD_LOCAL.set(user);
    }
}
