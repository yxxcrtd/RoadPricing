package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Parking;
import com.igoosd.mapper.ParkingMapper;
import com.igoosd.model.TParking;
import com.igoosd.rps.service.ParkingService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/9.
 */
@Service
public class ParkingServiceImpl extends AbsCommonService<TParking, Long> implements ParkingService {

    @Autowired
    private ParkingMapper parkingMapper;

    @Override
    protected BaseMapper<TParking> getMapper() {
        return parkingMapper;
    }

    @Override
    public Page<Parking> fuzzyFindPage(Page<Parking> page, Parking parking) {
        List<Parking> list = parkingMapper.fuzzyFindPage(page,parking);
        return  page.setRecords(list);
    }
}
