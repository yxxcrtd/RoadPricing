package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DevicePos;
import com.igoosd.mapper.DevicePosMapper;
import com.igoosd.model.TDevicePos;
import com.igoosd.rps.service.DevicePosService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 2018/5/10.
 */
@Service
public class DevicePosServiceImpl extends AbsCommonService<TDevicePos,Long> implements DevicePosService {


    @Autowired
    private DevicePosMapper devicePosMapper;


    @Override
    protected BaseMapper<TDevicePos> getMapper() {
        return devicePosMapper;
    }

    @Override
    public Page<DevicePos> fuzzyFindPage(DevicePos devicePos, Page page) {

        List<DevicePos> list = devicePosMapper.fuzzyFindPage(page,devicePos);
        return page.setRecords(list);
    }
}
