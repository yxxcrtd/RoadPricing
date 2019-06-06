package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DutyStatistics;
import com.igoosd.mapper.DutyStatisticsMapper;
import com.igoosd.model.TDutyStatistics;
import com.igoosd.rps.service.DutyStatisticsService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/14.
 */
@Service
public class DutyStatisticsServiceImpl extends AbsCommonService<TDutyStatistics,Long> implements DutyStatisticsService {

    @Autowired
    private DutyStatisticsMapper dutyStatisticsMapper;




    @Override
    protected BaseMapper<TDutyStatistics> getMapper() {
        return dutyStatisticsMapper;
    }

    @Override
    public Page<DutyStatistics> fuzzyFindPage(DutyStatistics dutyStatistics, Page<DutyStatistics> page) {
       List<DutyStatistics> list = dutyStatisticsMapper.fuzzyFindPage(page,dutyStatistics);
       return page.setRecords(list);
    }
}
