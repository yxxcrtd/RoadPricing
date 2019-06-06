package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.AccountSummary;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.rps.service.AccountSummaryService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/11.
 */
@Service
public class AccountSummaryServiceImpl extends AbsCommonService<TAccountSummary,Long> implements AccountSummaryService {

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;

    @Override
    protected BaseMapper<TAccountSummary> getMapper() {
        return accountSummaryMapper;
    }

    @Override
    public Page<AccountSummary> fuzzyFindPage(AccountSummary accountSummary, Page<AccountSummary> page) {
        List<AccountSummary> list =  accountSummaryMapper.fuzzyFindPage(page,accountSummary);
        return page.setRecords(list);
    }
}
