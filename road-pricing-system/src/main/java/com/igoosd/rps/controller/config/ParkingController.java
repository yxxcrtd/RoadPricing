package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.Parking;
import com.igoosd.model.TParking;
import com.igoosd.rps.service.ParkingService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "配置管理-停车场管理")
@RestController
@RequestMapping("/config/parking")
public class ParkingController {


    @Autowired
    private ParkingService parkingService;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "停车场名称",  dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "chargeRuleId", value = "收费规则（下拉框）", dataType = "int", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest,Parking parking){
        Page<Parking> page =  parkingService.fuzzyFindPage(new Page(pageRequest.getPageNumber(),pageRequest.getPageSize(),"createTime",false),parking);
        return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "停车场名称", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "chargeRuleId", value = "收费规则（下拉框）", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "totalParkingSpace", value = "车位数", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "地址",  dataType = "int", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(TParking parking){
        Assert.hasText(parking.getName(),"停车场名称不能为空");
        Assert.notNull(parking.getChargeRuleId(),"收费规则不能为空");

        TParking temp = new TParking();
        temp.setName(parking.getName());
        int count = parkingService.getCount(temp);
        Assert.isTrue(count == 0,"停车场名称重复");

        parking.setCreateTime(new Date());
        parkingService.insert(parking);
        return ResultMsg.resultSuccess();
    }

    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "停车场名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "chargeRuleId", value = "收费规则（下拉框）", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "totalParkingSpace", value = "车位数", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "地址",  dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(TParking parking){
        Assert.notNull(parking.getId(),"ID不能为空");
        Assert.hasText(parking.getName(),"停车场名称不能为空");
        Assert.notNull(parking.getChargeRuleId(),"收费规则不能为空");

        TParking oldParking = parkingService.getEntityByKey(parking.getId());
        Assert.notNull(oldParking,"找不到指定的停车场");
        //名称校验
        TParking temp = new TParking();
        temp.setName(parking.getName());
        List<TParking> list = parkingService.findList(temp);
        if(!CollectionUtils.isEmpty(list)){
            Assert.isTrue(list.size() == 1,"停车场名称重复");
            Assert.isTrue(list.get(0).getId().equals(parking.getId()),"停车场名称重复");
        }
        parkingService.update(parking);
        return ResultMsg.resultSuccess();
    }

    @PostMapping("findAll")
    @ApiOperation(value = "下拉框查询所有停车场", notes = "")
    public  ResultMsg findAll(){
        List<TParking> list = parkingService.findList(new TParking());
        return ResultMsg.resultSuccess(list);
    }

}
