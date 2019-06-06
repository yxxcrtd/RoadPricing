package com.igoosd.rpss.service.impl;

import com.igoosd.domain.SubParking;
import com.igoosd.mapper.SubParkingMapper;
import com.igoosd.rpss.service.SubParkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/2/5.
 */
@Service
@Slf4j
public class SubParkingServiceImpl implements SubParkingService {


    @Autowired
    private SubParkingMapper subParkingMapper;

    @Override
    public SubParking getSubParkingByKey(Long subParkingId) {
        return subParkingMapper.getSubParkingByKey(subParkingId);
    }
}
