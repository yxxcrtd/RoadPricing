package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.OrderItem;
import com.igoosd.model.TOrderItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-03-15
 */
public interface OrderItemMapper extends BaseMapper<TOrderItem> {


    /**
     * 批量新增订单
     * @return
     */
    int batchInsert(@Param("list") List<TOrderItem> list);


}
