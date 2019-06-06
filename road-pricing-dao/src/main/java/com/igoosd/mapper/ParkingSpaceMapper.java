package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.model.TParkingSpace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-02-02
 */
public interface ParkingSpaceMapper extends BaseMapper<TParkingSpace> {

    /**
     * 查询车位列表
     * @param offset
     * @param limit
     * @return
     */
    List<ParkingSpace> findParkingSpaceList(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 查找指定路段下的车位Id集合
     * @param subParkingId
     * @return
     */
    List<Long> findIdsBySubParkingId(@Param("subParkingId") Long subParkingId);

    /**
     * 管理平台模糊分页查询
     * @param page
     * @param parkingSpace
     * @return
     */
    List<ParkingSpace> fuzzyFindPage(Page page, ParkingSpace parkingSpace);

}
