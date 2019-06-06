package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/10.
 */
public interface ArrearsRecordService extends CommonService<TArrearsRecord,Long> {


    Page<ArrearsRecord> fuzzyFindPage(ArrearsRecord arrearsRecord, Page<ArrearsRecord> page);

    /**
     * 人工抹账
     * @param id
     */
    void wipeArrears(Long id);

}
