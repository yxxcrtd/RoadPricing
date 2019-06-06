package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Collector;
import com.igoosd.model.TCollector;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/9.
 */
public interface CollectorService extends CommonService<TCollector,Long> {


    /**
     * 模糊分页查询收费员
     * @param collector
     * @param page
     * @return
     */
    Page<Collector> fuzzyFindPage(Collector collector,Page page);
}
