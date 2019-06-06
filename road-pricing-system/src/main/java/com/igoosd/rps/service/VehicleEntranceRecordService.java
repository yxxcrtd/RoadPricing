package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/10.
 */
public interface VehicleEntranceRecordService extends CommonService<TVehicleEntranceRecord,Long> {

    /**
     * 分页查询停车记录列表
     * @param ver
     * @param page
     * @return
     */
    Page<VehicleEntranceRecord> fuzzyFindPage(VehicleEntranceRecord ver, Page<VehicleEntranceRecord> page);


    /**
     * 查看指定停车记录详情信息
     * @param id
     * @return
     */
    VehicleEntranceRecord getDetailVerByKey(Long id);
}
