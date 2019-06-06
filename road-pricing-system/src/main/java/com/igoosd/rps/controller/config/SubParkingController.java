package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.RegexUtils;
import com.igoosd.domain.SubParking;
import com.igoosd.model.TSubParking;
import com.igoosd.rps.service.SubParkingService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 2018/5/9.
 */
@RestController
@RequestMapping("/config/subParking")
@Api(tags = "配置管理-路段管理")
public class SubParkingController {

    @Autowired
    private SubParkingService subParkingService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "parkingId", value = "所属停车场（下拉框）", dataType = "long", paramType = "form"),
    })
    @PostMapping("page")
    public ResultMsg page(PageRequest pageRequest, SubParking subParking) {
        Page<SubParking> page = subParkingService.fuzzyFindPage(subParking,
                new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), "createTime", false));
        return ResultMsg.resultSuccess(page);
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "parkingId", value = "所属停车场（下拉框）", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "geoLocation", value = "地理坐标 '纬度,经度'", required = true, dataType = "string", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(SubParking subParking) {

        Assert.hasText(subParking.getName(), "名称不能为空");
        Assert.hasText(subParking.getCode(), "编码不能为空");
        Assert.notNull(subParking.getParkingId(), "所属停车场不能为空");
        Assert.hasText(subParking.getGeoLocation(), "地理坐标不能为空");
        //地理坐标校验
        boolean flag = RegexUtils.isGeoLocation(subParking.getGeoLocation());
        Assert.isTrue(flag, "请输入正确的地理位置（至少保留4位小数），格式为: '纬度,经度'");

        SubParking temp1 = new SubParking();
        temp1.setCode(subParking.getCode());
        int count = subParkingService.getCount(temp1);
        Assert.isTrue(count == 0, "路段编码重复");
        subParking.setCreateTime(new Date());
        subParkingService.insert(subParking);
        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "编码", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "parkingId", value = "所属停车场（下拉框）", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "geoLocation", value = "地理坐标 '纬度,经度'", dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(SubParking subParking) {
        Assert.notNull(subParking.getId(), "ID不能为空");
        if (!StringUtils.isEmpty(subParking.getCode())) {
            TSubParking temp = new TSubParking();
            temp.setCode(subParking.getCode());
            List<TSubParking> list = subParkingService.findList(temp);
            if (!CollectionUtils.isEmpty(list)) {
                Assert.isTrue(list.size() == 1, "路段编码重复");
                Assert.isTrue(list.get(0).getCode().equals(subParking.getCode()), "路段编码重复");
            }
        }
        //更新操作
        subParkingService.update(subParking);
        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "下拉框查询所有路段", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingId", value = "所属停车场", dataType = "long", paramType = "form"),
    })
    @PostMapping("findAll")
    public ResultMsg findAll(Long parkingId) {
        TSubParking temp = new TSubParking();
        temp.setParkingId(parkingId);
        List<TSubParking> list = subParkingService.findList(temp);
        return ResultMsg.resultSuccess(list);
    }

}
