package com.igoosd.rpss.service;

import com.igoosd.domain.ArrearsRecord;
import com.igoosd.rpss.vo.ArrearsDetailInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2018/2/7.
 */
public interface ArrearsService {

    /**
     * 查询车牌号总的欠费金额
     * @param carNumber
     * @return
     */
    BigDecimal getTotalArrearsAmount(String carNumber);


    /**
     * 根据车牌号查询欠费记录列表
     * @param carNumber
     * @return
     */
    List<ArrearsRecord> findArrearsListByCarNumber(String carNumber);


    /**
     * 获取欠费记录详情
     * @param arrearsRecordId
     * @return
     */
    ArrearsDetailInfo getArrearsDetailById(Long arrearsRecordId);


    /**
     * 根据停车记录ID查询欠费记录id集合
     * @param carNumber
     * @return
     */
    List<Long> findArrearsIdsByCarNumber(String carNumber);

}
