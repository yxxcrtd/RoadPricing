package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DutyRecord;
import com.igoosd.mapper.DutyRecordMapper;
import com.igoosd.model.TDutyRecord;
import com.igoosd.rps.service.DutyRecordService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/14.
 */
@Service
public class DutyRecordServiceImpl extends AbsCommonService<TDutyRecord,Long> implements DutyRecordService {

    @Autowired
    private DutyRecordMapper dutyRecordMapper;




    @Override
    protected BaseMapper<TDutyRecord> getMapper() {
        return dutyRecordMapper;
    }

    @Override
    public Page<DutyRecord> fuzzyFindPage(DutyRecord dutyRecord, Page<DutyRecord> page) {
        List<DutyRecord> list =  dutyRecordMapper.fuzzyFindPage(page,dutyRecord);

        return page.setRecords(list);
    }
}
