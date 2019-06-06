package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Collector;
import com.igoosd.mapper.CollectorMapper;
import com.igoosd.model.TCollector;
import com.igoosd.rps.service.CollectorService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/9.
 */
@Service
public class CollectorServiceImpl extends AbsCommonService<TCollector,Long> implements CollectorService {

    @Autowired
    private CollectorMapper collectorMapper;

    @Override
    protected BaseMapper<TCollector> getMapper() {
        return collectorMapper;
    }

    @Override
    public Page<Collector> fuzzyFindPage(Collector collector, Page page) {
       List<Collector> list =  collectorMapper.fuzzyFindPage(page,collector);
       return page.setRecords(list);
    }
}
