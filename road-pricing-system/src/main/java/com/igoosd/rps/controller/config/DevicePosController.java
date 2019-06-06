package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.DeviceTypeEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.Collector;
import com.igoosd.domain.DevicePos;
import com.igoosd.model.TDevicePos;
import com.igoosd.rps.service.DevicePosService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 2018/5/10.
 */
@RestController
@RequestMapping("/config/devicePos")
@Api(tags = "配置管理-设备管理")
public class DevicePosController {


    @Autowired
    private DevicePosService devicePosService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "deviceId", value = "设备号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bluetoothId", value = "蓝牙ID", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段（下拉框）", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "描述", dataType = "string", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, DevicePos devicePos) {

        Page<Collector> page = devicePosService.fuzzyFindPage(devicePos, new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), "id", false));
        return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备号", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bluetoothId", value = "蓝牙ID", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段", dataType = "long", required = true, paramType = "form"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型 0：通用PDA 1:p990 POS机", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "描述", dataType = "string", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(DevicePos devicePos) {
        Assert.hasText(devicePos.getDeviceId(), "设备Id不能为空");
        Assert.notNull(devicePos.getSubParkingId(), "路段不能为空");
        Assert.notNull(devicePos.getDeviceType(), "设备类型不能为空");

        Assert.notNull(DeviceTypeEnum.getDeviceTypeEnumByValue(devicePos.getDeviceType()), "不合法的设备类型");

        TDevicePos temp = new TDevicePos();
        temp.setDeviceId(devicePos.getDeviceId());

        int count = devicePosService.getCount(temp);
        Assert.isTrue(count == 0, "设备号重复");

        devicePosService.insert(devicePos);
        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID",required = true,dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "deviceId", value = "设备号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bluetoothId", value = "蓝牙ID", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型 0：通用PDA 1:p990 POS机", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "描述", dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(DevicePos devicePos) {
        Assert.notNull(devicePos.getId(), "ID不能为空");
        if (devicePos.getDeviceType() != null) {
            Assert.notNull(DeviceTypeEnum.getDeviceTypeEnumByValue(devicePos.getDeviceType()), "不合法的设备类型");
        }
        if (!StringUtils.isEmpty(devicePos.getDeviceId())) {
            TDevicePos temp = new TDevicePos();
            temp.setDeviceId(devicePos.getDeviceId());
            List<TDevicePos> list = devicePosService.findList(temp);
            if (!CollectionUtils.isEmpty(list)) {
                Assert.isTrue(list.size() == 1, "设备号重复");
                Assert.isTrue(list.get(0).getId().equals(devicePos.getId()), "设备号重复");
            }
        }

        devicePosService.update(devicePos);
        return ResultMsg.resultSuccess();
    }


}
