package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DutyStatistics;
import com.igoosd.model.TDutyStatistics;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/14.
 */
public interface DutyStatisticsService extends CommonService<TDutyStatistics,Long> {


    /**
     * 分页查询
     * @param dutyStatistics
     * @param page
     * @return
     */
    Page<DutyStatistics> fuzzyFindPage(DutyStatistics dutyStatistics, Page<DutyStatistics> page);

}
