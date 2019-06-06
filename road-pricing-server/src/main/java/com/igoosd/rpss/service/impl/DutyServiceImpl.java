package com.igoosd.rpss.service.impl;

import com.igoosd.mapper.DutyRecordMapper;
import com.igoosd.model.TDutyRecord;
import com.igoosd.rpss.service.DutyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 2018/3/27.
 */
@Service
@Slf4j
public class DutyServiceImpl implements DutyService {


    @Autowired
    private DutyRecordMapper dutyRecordMapper;

    @Override
    public Long saveLoginDutyRecord(Long collectionId,Long subParkingId,String location){

        TDutyRecord dutyRecord = new TDutyRecord();
        dutyRecord.setCollectorId(collectionId);
        dutyRecord.setLocation(location);
        dutyRecord.setSubParkingId(subParkingId);
        dutyRecord.setLoginTime(new Date());
        dutyRecordMapper.insert(dutyRecord);
        return dutyRecord.getId();
    }


    @Override
    public void updateLogoutDutyRecord(Long recordId) {
        TDutyRecord dutyRecord = new TDutyRecord();
        dutyRecord.setLogoutTime(new Date());
        dutyRecord.setId(recordId);
        dutyRecordMapper.updateById(dutyRecord);
    }
}
