package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.model.TParkingSpace;
import com.igoosd.model.TSubParking;
import com.igoosd.rps.service.ParkingSpaceService;
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

import java.util.List;

/**
 * 2018/5/9.
 */
@Api(tags = "配置管理-车位管理")
@RestController
@RequestMapping("/config/parkingSpace")
public class ParkingSpaceController {


    @Autowired
    private ParkingSpaceService parkingSpaceService;
    @Autowired
    private SubParkingService subParkingService;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "parkingId", value = "所属停车场（下拉框）", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "所属路段（下拉框）", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "编码",  dataType = "string", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, ParkingSpace parkingSpace){
        Page<ParkingSpace> page =  parkingSpaceService.fuzzyFindPage(new Page(pageRequest.getPageNumber(),pageRequest.getPageSize(),"createTime",false),parkingSpace);
        return ResultMsg.resultSuccess(page);
    }

    @ApiOperation(value = "批量信泽", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingId", value = "所属停车场", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "所属路段", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "splitChar", value = "分隔符", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "startNum", value = "开始序号", dataType = "int",  required = true,paramType = "form"),
            @ApiImplicitParam(name = "endNum", value = "截止序号",  dataType = "string", required = true,paramType = "form"),
    })
    @PostMapping("/batchInsert")
    public ResultMsg batchInsert(ParkingSpace parkingSpace){
        //校验
        Assert.notNull(parkingSpace.getParkingId(),"停车场不能为空");
        Assert.notNull(parkingSpace.getSubParkingId(),"路段不能为空");
        Assert.notNull(parkingSpace.getStartNum()," 开始序号不能为空");
        Assert.notNull(parkingSpace.getEndNum()," 截止序号不能为空");
        Assert.isTrue(parkingSpace.getEndNum() >= parkingSpace.getStartNum(),"截止序号不能小于开始序号");
        Assert.isTrue(parkingSpace.getEndNum()<100,"开始或截止序号范围为0-99");
        Assert.isTrue(parkingSpace.getStartNum()>=0,"开始或截止序号范围为0-99");

        TSubParking subParking = subParkingService.getEntityByKey(parkingSpace.getSubParkingId());
        Assert.notNull(subParking,"找不到指定的路段");
        Assert.isTrue(subParking.getParkingId().equals(parkingSpace.getParkingId()),"路段所属停车场不匹配");

        parkingSpaceService.batchInsert(parkingSpace);
        return ResultMsg.resultSuccess();
    }

    @ApiOperation(value = "批量信泽", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "code", value = "编码",dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "parkingId", value = "所属停车场",  dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "所属路段", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "geoSensorCode", value = "地磁编码", dataType = "string",  required = true,paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(ParkingSpace parkingSpace){

        Assert.notNull(parkingSpace.getId(),"ID不能为空");

        TParkingSpace oldPs = parkingSpaceService.getEntityByKey(parkingSpace.getId());
        Assert.notNull(oldPs,"找不到指定的停车位信息");
        if(!StringUtils.isEmpty(parkingSpace.getCode())){
            TParkingSpace temp = new TParkingSpace();
            temp.setCode(parkingSpace.getCode());
            List<TParkingSpace> list = parkingSpaceService.findList(temp);
            if(!CollectionUtils.isEmpty(list)){
                Assert.isTrue(list.size() == 1,"停车位编码重复");
                Assert.isTrue(oldPs.getCode().equals(list.get(0).getCode()),"停车位编码重复");
            }
        }
        parkingSpaceService.update(parkingSpace);
        return ResultMsg.resultSuccess();
    }

}
