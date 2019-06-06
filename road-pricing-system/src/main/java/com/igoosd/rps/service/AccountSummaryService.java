package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.AccountSummary;
import com.igoosd.model.TAccountSummary;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/11.
 */
public interface AccountSummaryService extends CommonService<TAccountSummary,Long> {


    /**
     * 分页查询扎帐列表
     * @param accountSummary
     * @param page
     * @return
     */
    Page<AccountSummary> fuzzyFindPage(AccountSummary accountSummary,Page<AccountSummary> page);

}
