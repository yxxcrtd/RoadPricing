package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.mapper.ParkingSpaceMapper;
import com.igoosd.mapper.SubParkingMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TParkingSpace;
import com.igoosd.model.TSubParking;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rps.service.ParkingSpaceService;
import com.igoosd.rps.util.AbsCommonService;
import com.igoosd.rps.vo.ParkingSpaceRatio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 2018/5/9.
 */
@Service
public class ParkingSpaceServiceImpl extends AbsCommonService<TParkingSpace, Long> implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;

    @Autowired
    private SubParkingMapper subParkingMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;

    @Override
    protected BaseMapper<TParkingSpace> getMapper() {
        return parkingSpaceMapper;
    }

    @Override
    public Page<ParkingSpace> fuzzyFindPage(Page page, ParkingSpace parkingSpace) {
        List<ParkingSpace> list = parkingSpaceMapper.fuzzyFindPage(page, parkingSpace);
        return page.setRecords(list);
    }

    @Transactional
    @Override
    public void batchInsert(ParkingSpace parkingSpace) {
        int starNum = parkingSpace.getStartNum();
        int endNum = parkingSpace.getEndNum();
        int count = endNum - starNum;
        TSubParking tsp = subParkingMapper.selectById(parkingSpace.getSubParkingId());
        String splitChar = StringUtils.isEmpty(parkingSpace.getSplitChar()) ? "" : parkingSpace.getSplitChar();
        splitChar = splitChar.trim();

        TParkingSpace tps = new TParkingSpace();
        tps.setParkingId(parkingSpace.getParkingId());
        tps.setSubParkingId(parkingSpace.getSubParkingId());
        tps.setCreateTime(new Date());
        for (int i = 0; i <= count; i++) {
            String code = tsp.getCode() + splitChar + String.format("%02d", i + starNum);
            //校验
            TParkingSpace temp = new TParkingSpace();
            temp.setCode(code);
            Assert.isTrue(getCount(temp) ==0,"编码：{}重复,批量新增失败",code);
            tps.setCode(code);
            parkingSpaceMapper.insert(tps);
        }
    }

    @Override
    public ParkingSpaceRatio getParkinbgSpaceRatio() {
        Integer[] onlineStatus = {BizStatusEnum.CAR_ENTER.getValue(),BizStatusEnum.EXIT_PAYING.getValue()};
        //total
        int total = parkingSpaceMapper.selectCount(new EntityWrapper<>());
        //online
        int online = vehicleEntranceRecordMapper.selectCount(new EntityWrapper<TVehicleEntranceRecord>()
                .ge("create_time", DateUtil.convertYmdDate(new Date()))
                .in("biz_status",onlineStatus));
        ParkingSpaceRatio ratio = new ParkingSpaceRatio();
        ratio.setTotalCount(total);
        ratio.setUsingCount(online);
        ratio.setUnusedCount(total - online);
        return ratio;
    }
}
