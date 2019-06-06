package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.mapper.SysConfigMapper;
import com.igoosd.model.TSysConfig;
import com.igoosd.rps.service.SysConfigService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/4/9.
 */
@Service
public class SysConfigServiceImpl extends AbsCommonService<TSysConfig,Integer> implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;


    @Override
    protected BaseMapper<TSysConfig> getMapper() {
        return sysConfigMapper;
    }
}
