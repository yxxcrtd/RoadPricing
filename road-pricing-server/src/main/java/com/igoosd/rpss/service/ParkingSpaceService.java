package com.igoosd.rpss.service;

import java.util.List;

/**
 * 2018/2/5.
 */
public interface ParkingSpaceService {


    /**
     * 车位初始化操作
     */
    void init();
    /**
     * 查找指定路段下的车位ids
      * @param subParkingId
     * @return
     */
    List<Long> findIdsBySubParkingId(Long subParkingId);
}
