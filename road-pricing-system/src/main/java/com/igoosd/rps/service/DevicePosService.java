package com.igoosd.rps.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.domain.DevicePos;
import com.igoosd.model.TDevicePos;
import com.igoosd.rps.util.CommonService;

/**
 * 2018/5/10.
 * 设备Pos机服务
 */
public interface DevicePosService extends CommonService<TDevicePos,Long>{


    /**
     * 分页查询 设备列表
     * @param devicePos
     * @param page
     * @return
     */
    Page<DevicePos> fuzzyFindPage(DevicePos devicePos, Page<DevicePos> page);
}
