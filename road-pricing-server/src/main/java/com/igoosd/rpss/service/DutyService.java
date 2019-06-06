package com.igoosd.rpss.service;

/**
 * 2018/3/27.
 * 执勤服务
 */
public interface DutyService {

    Long saveLoginDutyRecord(Long collectionId,Long subParkingId,String location);

    void updateLogoutDutyRecord(Long recordId);
}
