package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.Parking;
import com.igoosd.model.TParking;
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
public interface ParkingMapper extends BaseMapper<TParking> {


    List<Parking> fuzzyFindPage(RowBounds rb, Parking parking);
}
