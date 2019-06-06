package com.igoosd.rps.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 2018/5/14.
 * 收入汇总
 */
@Data
public class IncomeSummary {

    private BigDecimal totalIncomeForCurDay;

    private BigDecimal onlineIncomeForCurDay;

    private BigDecimal offlineIncomeForCurDay;

    private BigDecimal totalIncomeForCurMonth;

    private BigDecimal onlineIncomeForCurMonth;

    private BigDecimal offlineIncomeForCurMonth;
}
