package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.model.TParkingSpace;
import com.igoosd.rps.util.CommonService;
import com.igoosd.rps.vo.ParkingSpaceRatio;

/**
 * 2018/5/9.
 */
public interface ParkingSpaceService extends CommonService<TParkingSpace,Long> {

    Page<ParkingSpace> fuzzyFindPage(Page page, ParkingSpace parkingSpace);

    void batchInsert(ParkingSpace parkingSpace);


    /**
     * 获取当前车位使用率
     * @return
     */
    ParkingSpaceRatio getParkinbgSpaceRatio();
}
