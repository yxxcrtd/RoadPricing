package com.igoosd.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.igoosd.domain.DevicePos;
import com.igoosd.model.TDevicePos;
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
public interface DevicePosMapper extends BaseMapper<TDevicePos> {


    List<DevicePos> fuzzyFindPage(RowBounds rb,DevicePos devicePos);
}
