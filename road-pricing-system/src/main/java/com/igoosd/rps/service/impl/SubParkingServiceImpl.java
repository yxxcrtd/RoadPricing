package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.SubParking;
import com.igoosd.mapper.SubParkingMapper;
import com.igoosd.model.TSubParking;
import com.igoosd.rps.service.SubParkingService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/9.
 */
@Service
public class SubParkingServiceImpl extends AbsCommonService<TSubParking,Long> implements SubParkingService {

    @Autowired
    private SubParkingMapper subParkingMapper;

    @Override
    protected BaseMapper<TSubParking> getMapper() {
        return subParkingMapper;
    }

    @Override
    public Page<SubParking> fuzzyFindPage(SubParking subParking, Page page) {
        List<SubParking> list = subParkingMapper.fuzzyfindPage(page,subParking);
        return page.setRecords(list);
    }
}
