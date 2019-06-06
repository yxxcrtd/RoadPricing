package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rps.service.VehicleEntranceRecordService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/10.
 */
@Service
public class VehicleEntranceRecordServiceImpl extends AbsCommonService<TVehicleEntranceRecord, Long> implements VehicleEntranceRecordService {

    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;


    @Override
    protected BaseMapper<TVehicleEntranceRecord> getMapper() {
        return vehicleEntranceRecordMapper;
    }

    @Override
    public Page<VehicleEntranceRecord> fuzzyFindPage(VehicleEntranceRecord ver, Page<VehicleEntranceRecord> page) {
        List<VehicleEntranceRecord> list = vehicleEntranceRecordMapper.fuzzyFindPage(page, ver);
        return page.setRecords(list);
    }

    @Override
    public VehicleEntranceRecord getDetailVerByKey(Long id) {
        return vehicleEntranceRecordMapper.getDetailVerByKey(id);
    }
}
