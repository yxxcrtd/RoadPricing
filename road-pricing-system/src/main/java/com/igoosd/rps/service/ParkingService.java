package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.Parking;
import com.igoosd.model.TParking;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/9.
 */
public interface ParkingService extends CommonService<TParking, Long> {


    /**
     * 模糊分页查询
     * @param page
     * @param parking
     * @return
     */
    Page<Parking> fuzzyFindPage(Page<Parking> page, Parking parking);
}
