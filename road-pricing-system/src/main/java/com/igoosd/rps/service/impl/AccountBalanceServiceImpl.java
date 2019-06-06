package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.AccountBalance;
import com.igoosd.mapper.AccountBalanceMapper;
import com.igoosd.model.TAccountBalance;
import com.igoosd.rps.service.AccountBalanceService;
import com.igoosd.rps.util.AbsCommonService;
import com.igoosd.rps.vo.Income;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/5/11.
 */
@Service
public class AccountBalanceServiceImpl extends AbsCommonService<TAccountBalance, Long> implements AccountBalanceService {

    @Autowired
    private AccountBalanceMapper accountBalanceMapper;

    @Override
    protected BaseMapper<TAccountBalance> getMapper() {
        return accountBalanceMapper;
    }


    @Override
    public Page<AccountBalance> fuzzyFindPage(AccountBalance accountBalance, Page<AccountBalance> page) {
        List<AccountBalance> list = accountBalanceMapper.fuzzyFindPage(page, accountBalance);
        return page.setRecords(list);
    }

    @Override
    public List<TAccountBalance> findListByCurMonth() {
        Date startDate = DateUtil.getFistDayForCurMonth();
        Date endDate = DateUtil.convertYmdDate(new Date());
        return accountBalanceMapper.selectList(new EntityWrapper<TAccountBalance>()
                .ge("balance_date", startDate)
                .lt("balance_date", endDate));
    }

    @Override
    public List<Income> getIncomeListByQueryWay(int queryWay) {
        //按日查询近七天的收入情况
        List<Income> rstList = new ArrayList<>();
        int count;
        if (0 == queryWay) {
            count = 7;
            Date endDate = DateUtil.getLastDate(new Date());
            Date startDate = DateUtil.getDate(endDate, -6);
            List<TAccountBalance> list = accountBalanceMapper.selectList(new EntityWrapper<TAccountBalance>()
                    .between("balance_date", startDate, endDate).orderBy("balance_date", true));

            if (null == list) {
                rstList = new ArrayList<>();
            }
            int j = count - list.size();
            int i = 0;
            for (; i < j; i++) {
                Date date = DateUtil.getDate(startDate, i);
                rstList.add(new Income(DateUtil.formatYmdDate(date), BigDecimal.ZERO));
            }
            for (TAccountBalance tab : list) {
                rstList.add(new Income(DateUtil.formatYmdDate(tab.getBalanceDate()), tab.getTotalRealChargeAmount()));
            }
        } else if (1 == queryWay) {
            count = 12;
            Date firstDayOfCurMonth = DateUtil.getFistDayForCurMonth();
            String pattern = "yyyy-MM";
            for (int i = count; i >= 1; i--) {
                Date firstDay = DateUtil.getMonth(firstDayOfCurMonth, -i);
                Date nextFirstDay = DateUtil.getMonth(firstDayOfCurMonth, -(i - 1));
                BigDecimal totalAmount = accountBalanceMapper.getTotalAmount(firstDay, nextFirstDay);
                rstList.add(new Income(DateUtil.formatDate(firstDay, pattern), totalAmount == null ? BigDecimal.ZERO : totalAmount));
            }
        }
        return rstList;
    }
}
