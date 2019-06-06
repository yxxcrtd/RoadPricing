package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.mapper.ChargeRuleMapper;
import com.igoosd.model.TChargeRule;
import com.igoosd.rps.service.ChargeRuleService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/5/9.
 */
@Service
public class ChargeRuleServiceImpl extends AbsCommonService<TChargeRule,Long> implements ChargeRuleService {

    @Autowired
    private ChargeRuleMapper chargeRuleMapper;


    @Override
    protected BaseMapper<TChargeRule> getMapper() {
        return chargeRuleMapper;
    }
}
