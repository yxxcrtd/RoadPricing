package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.DutyRecord;
import com.igoosd.model.TDutyRecord;
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
public interface DutyRecordMapper extends BaseMapper<TDutyRecord> {


    /**
     * 获取当日执勤收费员列表
     * @return
     */
     List<Long> getCurDateDutyCollectorIds();


    /**
     * 分页查询执勤登录日志
     * @return
     */
     List<DutyRecord> fuzzyFindPage(RowBounds rb,DutyRecord dutyRecord);

}
