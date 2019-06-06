package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.SubParking;
import com.igoosd.model.TSubParking;
import org.apache.ibatis.annotations.Param;
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
public interface SubParkingMapper extends BaseMapper<TSubParking> {


    SubParking getSubParkingByKey(@Param("id") Long subParkingId);


    List<SubParking> fuzzyfindPage(RowBounds rb,SubParking subParking);

}
