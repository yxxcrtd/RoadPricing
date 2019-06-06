package com.igoosd.rpss.service;

import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.model.TVehicleEntranceRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/2/8.
 */
public interface VehicleEntranceRecordService {

    /**
     * 车辆入场
     */
    Long enterCar(String carNumber, Long parkingSpaceId);


    /**
     * 欠费离场
     *
     * @param vehicleEntranceRecordId
     */
    void arrearsExit(Long vehicleEntranceRecordId,BigDecimal chargeAmount,BigDecimal realCharegeAmount,Date preExitTime);

    /**
     * 免费离场
     *
     */
    void freeExit(Long parkingSpaceId, Long vehicleEntranceRecordId, Long parkingId,Date preExitTime,BigDecimal chargeAmount);


    /**
     * 获取当前停车信息
     *
     * @param carNumber
     * @return
     */
    VehicleEntranceRecord getCurDateParkingVerInfo(String carNumber);


    /**
     * 获取当前时间未出场的停车记录列表
     * @return
     */
    List<TVehicleEntranceRecord> getNotExitParkingRecordsForCurrentDate();


    TVehicleEntranceRecord getVerById(Long verId);


    /**
     * 获取最终出场时间
     * @return
     */
    Date getFinalExitTime(Long parkingId,Date preExitTime);
}
