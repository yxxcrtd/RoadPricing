package com.igoosd.rpss.service.impl;

import com.igoosd.mapper.DevicePosMapper;
import com.igoosd.model.TDevicePos;
import com.igoosd.rpss.service.DevicePosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/2/5.
 */
@Service
@Slf4j
public class DevicePosServiceImpl implements DevicePosService {

    @Autowired
    private DevicePosMapper devicePosMapper;

    @Override
    public TDevicePos getTDevicePosByDeviceId(String deviceId) {

        TDevicePos tdp = new TDevicePos();
        tdp.setDeviceId(deviceId);
        return devicePosMapper.selectOne(tdp);
    }
}
