package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.DutyStatistics;
import com.igoosd.model.TDutyStatistics;
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
public interface DutyStatisticsMapper extends BaseMapper<TDutyStatistics> {


    List<DutyStatistics> fuzzyFindPage(RowBounds rb, DutyStatistics dutyStatistics);

}
