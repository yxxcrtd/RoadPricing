package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.AccountBalance;
import com.igoosd.model.TAccountBalance;
import com.igoosd.rps.util.CommonService;
import com.igoosd.rps.vo.Income;

import java.util.List;

/**
 * 2018/5/11.
 */
public interface AccountBalanceService extends CommonService<TAccountBalance,Long> {

    /**
     * 分页查询
     * @param accountBalance
     * @param page
     * @return
     */
    Page<AccountBalance> fuzzyFindPage(AccountBalance accountBalance, Page<AccountBalance> page);


    List<TAccountBalance> findListByCurMonth();

    /**
     * 根据查询方式获取收入的列表
     * @param queryWay
     * @return
     */
    List<Income> getIncomeListByQueryWay(int queryWay);
}
