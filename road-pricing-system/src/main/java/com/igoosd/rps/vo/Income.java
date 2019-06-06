package com.igoosd.rps.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 2018/5/14.
 * 柱状图/折线图 key
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Income {

    private String key;

    private BigDecimal totalIncome;
}
