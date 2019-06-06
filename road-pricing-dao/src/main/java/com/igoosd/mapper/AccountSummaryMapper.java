package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.AccountSummary;
import com.igoosd.model.TAccountSummary;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface AccountSummaryMapper extends BaseMapper<TAccountSummary> {


    /**
     * 分頁查扎帐记录
     * @param rb
     * @param accountSummary
     * @return
     */
    List<AccountSummary> fuzzyFindPage(RowBounds rb,AccountSummary accountSummary);
}
