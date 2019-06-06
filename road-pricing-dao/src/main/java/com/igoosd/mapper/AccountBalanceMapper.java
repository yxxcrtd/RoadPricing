package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.AccountBalance;
import com.igoosd.model.TAccountBalance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 对账记录表 Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-03-30
 */
public interface AccountBalanceMapper extends BaseMapper<TAccountBalance> {

    /**
     * 分页查询列表
     * @param rb
     * @param accountBalance
     * @return
     */
    List<AccountBalance> fuzzyFindPage(RowBounds rb,AccountBalance accountBalance);

    /**
     * [startDate,endDate)
     * @param startDate
     * @param endDate
     * @return
     */
    BigDecimal getTotalAmount(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
