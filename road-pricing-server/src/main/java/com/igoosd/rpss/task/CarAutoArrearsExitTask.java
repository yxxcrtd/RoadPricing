package com.igoosd.rpss.task;

import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.service.ParkingSpaceService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/16.
 * 定时调度 未驶出车联 统一欠费离场
 */
@Service
@Slf4j
@Order(2)
public class CarAutoArrearsExitTask extends AbsTask {

    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;
    @Autowired
    private ChargeRuleService chargeRuleService;
    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Scheduled(cron = "0 10 22 * * ?")
    public void doTask() {
        if(!preTask()){
            return ;
        }
        List<TVehicleEntranceRecord> list = vehicleEntranceRecordService.getNotExitParkingRecordsForCurrentDate();
        if (!CollectionUtils.isEmpty(list)) {
            Date preExitDate = new Date();
            for (TVehicleEntranceRecord ver : list) {
                BigDecimal realChargeAmount = chargeRuleService.getRealChargeAmount(ver.getParkingId(), ver.getSubParkingId(), ver.getEnterTime(), preExitDate, ver.getCarNumber());
                BigDecimal chargeAmount = chargeRuleService.getChargeAmount(ver.getParkingId(), ver.getEnterTime(), preExitDate);
                if (realChargeAmount.compareTo(BigDecimal.ZERO) == 0) {
                    //免费离场
                    vehicleEntranceRecordService.freeExit(ver.getParkingSpaceId(), ver.getId(), ver.getParkingId(), preExitDate, chargeAmount);
                } else {
                    //欠费离场
                    vehicleEntranceRecordService.arrearsExit(ver.getId(), chargeAmount, realChargeAmount, preExitDate);
                }
            }
        }
        //初始化车位 确保管理平台录入的车位能够正常使用
        parkingSpaceService.init();
        log.info("车辆统一欠费离场处理完成....");
    }


}
