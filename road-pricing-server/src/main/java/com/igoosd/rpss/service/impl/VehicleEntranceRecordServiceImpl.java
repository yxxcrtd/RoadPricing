package com.igoosd.rpss.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.ParkingSpaceStatusEnum;
import com.igoosd.common.enums.PaymentStatusEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.domain.Member;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.mapper.ParkingSpaceMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TChargeRule;
import com.igoosd.model.TParkingSpace;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import com.igoosd.rpss.util.RpssConst;
import com.igoosd.rpss.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/2/8.
 */
@Service
@Slf4j
public class VehicleEntranceRecordServiceImpl implements VehicleEntranceRecordService {


    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ChargeRuleService chargeRuleService;
    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private ConfigService configService;

    /**
     * 车辆入场
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long enterCar(String carNumber, Long parkingSpaceId) {
        LoginUser loginUser = LoginUser.get();
        //车位是否被占用校验
        ParkingSpace ps = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        Assert.isTrue(ps.getStatus() == ParkingSpaceStatusEnum.IDLE.getValue(), "当前车位非空闲车位,请检查车位信息");
        //查询车辆是否存在异常出入场记录
        VehicleEntranceRecord oldVer = vehicleEntranceRecordMapper.getEntranceWithoutExitRecord(carNumber);
        Assert.isTrue(oldVer == null, "入场异常;车辆:" + carNumber +"入场未出场");

        //查询车位版本快照
        TParkingSpace dbPs = parkingSpaceMapper.selectById(parkingSpaceId);
        //会员查询
        Member member = memberMapper.getMemberInfoByCarNumber(carNumber, loginUser.getSubParkingId());
        member = member == null ? new Member() : member;
        VehicleEntranceRecord ver = new VehicleEntranceRecord();
        ver.setCarNumber(carNumber);
        ver.setParkingSpaceId(parkingSpaceId);
        ver.setSubParkingId(loginUser.getSubParkingId());
        ver.setParkingId(loginUser.getParkingId());
        ver.setEnterCollectorId(loginUser.getCollectorId());
        ver.setEnterTime(new Date());
        ver.setCreateTime(new Date());
        ver.setMemberId(member.getId());
        ver.setUpdateTime(ver.getCreateTime());
        ver.setBizStatus(BizStatusEnum.CAR_ENTER.getValue());
        ver.setVersionNo(dbPs.getVersionNo() + 1);
        try {
            vehicleEntranceRecordMapper.insert(ver);
            //车位版本快照 事务下同步更新
            TParkingSpace tmp = new TParkingSpace();
            tmp.setId(parkingSpaceId);
            tmp.setVersionNo(ver.getVersionNo());
            parkingSpaceMapper.updateById(tmp);
        } catch (Exception e) {
            log.error("出入场数据插入异常;", e);
            throw new RoadPricingException("重复入场操作,本次操作拒绝");
        }
        //redis 同步缓存 入场数据
        ParkingSpace cachePs = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        BigDecimal totalAmount = arrearsRecordMapper.getTotalArrearsAmountByCarNumber(carNumber);

        //车位更新
        cachePs.setStatus(ParkingSpaceStatusEnum.USING.getValue());
        cachePs.setCarNumber(carNumber);
        cachePs.setTotalArrearsAmount(totalAmount);
        cachePs.setVehicleEntranceRecordId(ver.getId());
        cachePs.setMemberStartDate(member.getStartDate());
        cachePs.setMemberEndDate(member.getEndDate());
        cachePs.setMemberTypeId(member.getMemberTypeId());
        cachePs.setMemberTypeName(member.getMemberTypeName());
        cachePs.setEnterTime(ver.getEnterTime());
        redisCacheService.setParkingSpaceInfo(cachePs);
        return ver.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void arrearsExit(Long vehicleEntranceRecordId,BigDecimal chargeAmount,BigDecimal realCharegeAmount,Date preExitTime) {

        TVehicleEntranceRecord ver = vehicleEntranceRecordMapper.selectById(vehicleEntranceRecordId);
        //出入场记录更新 update
        VehicleEntranceRecord temp = new VehicleEntranceRecord();
        temp.setExitCollectorId(LoginUser.get()==null? ver.getEnterCollectorId() : LoginUser.get().getCollectorId());
        temp.setUpdateTime(new Date());

        temp.setBizStatus(BizStatusEnum.ARREARS_EXIT.getValue());
        temp.setId(ver.getId());
        temp.setExitTime(getFinalExitTime(ver.getParkingId(),preExitTime));
        temp.setChargeAmount(chargeAmount);
        temp.setRealChargeAmount(realCharegeAmount);
        vehicleEntranceRecordMapper.updateById(temp);
        //欠费记录 insert
        ArrearsRecord ar = new ArrearsRecord();
        ar.setPaymentStatus(PaymentStatusEnum.UNPAID.getValue());
        ar.setCarNumber(ver.getCarNumber());
        ar.setArrearsAmount(temp.getRealChargeAmount());
        ar.setCreateTime(new Date());
        ar.setVehicleEntranceId(ver.getId());
        arrearsRecordMapper.insert(ar);
        redisCacheService.carExit(ver.getId(), ver.getParkingSpaceId(), Boolean.parseBoolean(configService.getValue(RpssConst.cfg_key_car_free_exit_2nd_confirm)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void freeExit(Long parkingSpaceId, Long vehicleEntranceRecordId, Long parkingId,Date preExitTime,BigDecimal chargeAmount) {
        VehicleEntranceRecord record = new VehicleEntranceRecord();
        record.setUpdateTime(new Date());
        record.setRealChargeAmount(BigDecimal.ZERO);
        record.setBizStatus(BizStatusEnum.CAR_EXIT.getValue());
        record.setExitCollectorId(LoginUser.get().getCollectorId());
        record.setExitTime(getFinalExitTime(parkingId,preExitTime));
        record.setChargeAmount(chargeAmount);
        record.setId(vehicleEntranceRecordId);
        vehicleEntranceRecordMapper.updateById(record);

        //缓存清除
        redisCacheService.carExit(vehicleEntranceRecordId, parkingSpaceId, Boolean.parseBoolean(configService.getValue(RpssConst.cfg_key_car_free_exit_2nd_confirm)));
    }

    @Override
    public VehicleEntranceRecord getCurDateParkingVerInfo(String carNumber) {
        return vehicleEntranceRecordMapper.getCurDateParkingVerInfo(carNumber);
    }

    @Override
    public List<TVehicleEntranceRecord> getNotExitParkingRecordsForCurrentDate() {
        Integer[] bizStatusArray = new Integer[2];
        bizStatusArray[0] = BizStatusEnum.CAR_ENTER.getValue();
        bizStatusArray[1] = BizStatusEnum.EXIT_PAYING.getValue();
        List<TVehicleEntranceRecord> list = vehicleEntranceRecordMapper.selectList(new EntityWrapper<TVehicleEntranceRecord>()
                .in("biz_status", bizStatusArray)
                .between("create_time", DateUtil.convertYmdDate(new Date()), new Date()));
        return list;
    }

    @Override
    public TVehicleEntranceRecord getVerById(Long verId) {

        return vehicleEntranceRecordMapper.selectById(verId);
    }

    @Override
    public Date getFinalExitTime(Long parkingId, Date preExitTime) {
        TChargeRule chargeRule = chargeRuleService.getChargeRuleByParkingId(parkingId);
        //预出场当天下班时间
        Date offWorkTime = DateUtil.splitDateAndTime(preExitTime, chargeRule.getEndTime());
        return offWorkTime.compareTo(preExitTime) > 0 ? preExitTime : offWorkTime;
    }

}
