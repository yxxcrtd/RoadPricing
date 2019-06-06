package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.Collector;
import com.igoosd.model.TCollector;
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
public interface CollectorMapper extends BaseMapper<TCollector> {

    List<Collector> fuzzyFindPage(RowBounds rb, Collector collector);
}
