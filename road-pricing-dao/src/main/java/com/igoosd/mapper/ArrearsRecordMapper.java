package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.model.TArrearsRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface ArrearsRecordMapper extends BaseMapper<TArrearsRecord> {


    /**
     * 获取车牌号的总欠费金额
     * @param carNumber
     * @return
     */
    BigDecimal getTotalArrearsAmountByCarNumber(@Param("carNumber")String carNumber);


    /**
     * 查询车牌号的历史欠费列表
     * @param carNumber
     * @return
     */
    List<ArrearsRecord> findArrearsListByCarNumber(@Param("carNumber")String carNumber);

    /**
     * 批量更新接口
     * @param ids
     * @return
     */
    int confirmOrderForArrearsRecords(@Param("paymentStatus") int paymentStatus,@Param("list") List<Long> ids);


    /**
     * 获取欠费记录 日缴纳的
     * @param startDate
     * @return
     */
    BigDecimal getSumArrearsChargeAmount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Long> findArrearsIdsByCarNumber(@Param("carNumber")String carNumber);


    List<ArrearsRecord> fuzzyFindPage(RowBounds rb,ArrearsRecord arrearsRecord);
}
