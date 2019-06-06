package com.igoosd.rpss.service.impl;

import com.igoosd.common.Assert;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.CollectorMapper;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.mapper.MemberTypeMapper;
import com.igoosd.mapper.ParkingMapper;
import com.igoosd.mapper.ParkingSpaceMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TCollector;
import com.igoosd.model.TMember;
import com.igoosd.model.TMemberType;
import com.igoosd.model.TParking;
import com.igoosd.model.TParkingSpace;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.service.VerPictureService;
import com.igoosd.rpss.vo.ArrearsDetailInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2018/2/7.
 */
@Service
@Slf4j
public class ArrearsServiceImpl implements ArrearsService {

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    private CollectorMapper collectorMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberTypeMapper memberTypeMapper;
    @Autowired
    private VerPictureService verPictureService;

    @Override
    public BigDecimal getTotalArrearsAmount(String carNumber) {
        return arrearsRecordMapper.getTotalArrearsAmountByCarNumber(carNumber);
    }

    @Override
    public List<ArrearsRecord> findArrearsListByCarNumber(String carNumber) {
        return arrearsRecordMapper.findArrearsListByCarNumber(carNumber);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArrearsDetailInfo getArrearsDetailById(Long arrearsRecordId) {
        TArrearsRecord tar = arrearsRecordMapper.selectById(arrearsRecordId);
        Assert.notNull(tar,"找不到指定的欠费记录");
        TVehicleEntranceRecord tver = vehicleEntranceRecordMapper.selectById(tar.getVehicleEntranceId());
        TParking tp = parkingMapper.selectById(tver.getParkingId());
        TParkingSpace tps = parkingSpaceMapper.selectById(tver.getParkingSpaceId());
        TCollector enterCol =collectorMapper.selectById(tver.getEnterCollectorId());
        TCollector exitCol = collectorMapper.selectById(tver.getExitCollectorId());
        List<String> list = verPictureService.getDetailPictureUrlsForVerId(tar.getVehicleEntranceId());

        ArrearsDetailInfo  detailInfo = new ArrearsDetailInfo();
        detailInfo.setCarNumber(tar.getCarNumber());
        detailInfo.setParkingSpaceCode(tps.getCode());
        detailInfo.setChargeAmount(tver.getChargeAmount());
        detailInfo.setRealChargeAmount(tver.getRealChargeAmount());
        detailInfo.setBizStatus(tver.getBizStatus());
        detailInfo.setParkingName(tp.getName());
        detailInfo.setParkingAddress(tp.getAddress());
        detailInfo.setEnterTime(tver.getEnterTime());
        detailInfo.setExitTime(tver.getExitTime());
        detailInfo.setEnterCollector(enterCol.getJobNumber());
        detailInfo.setExitCollector(exitCol.getJobNumber());
        detailInfo.setCarPictures(list);
        if(null != tver.getMemberId()){
            TMember member = memberMapper.selectById(tver.getMemberId());
            TMemberType tMemberType = memberTypeMapper.selectById(member.getMemberTypeId());
            detailInfo.setMemberTypeName(tMemberType.getName());
            detailInfo.setMemberStartDate(member.getStartDate());
            detailInfo.setMemberEndDate(member.getEndDate());
        }
        return detailInfo;
    }

    @Override
    public List<Long> findArrearsIdsByCarNumber(String carNumber) {
        return arrearsRecordMapper.findArrearsIdsByCarNumber(carNumber);
    }
}
