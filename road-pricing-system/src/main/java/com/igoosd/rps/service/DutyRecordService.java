package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DutyRecord;
import com.igoosd.model.TDutyRecord;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/14.
 */
public interface DutyRecordService extends CommonService<TDutyRecord,Long> {


    Page<DutyRecord> fuzzyFindPage(DutyRecord dutyRecord,Page<DutyRecord> page);
}
