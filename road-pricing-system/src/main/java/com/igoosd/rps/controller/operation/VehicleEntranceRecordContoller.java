package com.igoosd.rps.controller.operation;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.rps.service.VehicleEntranceRecordService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2018/5/10.
 */
@Api(tags = "运营管理-停车记录")
@RestController
@RequestMapping("/operation/ver")
public class VehicleEntranceRecordContoller {


    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;

    @ApiOperation(value = "分页查询", notes = "业务状态：    CAR_ENTER(0,\"车辆驶入\"),EXIT_PAYING(1,\"驶出支付中\"), CAR_EXIT(2,\"缴费驶出\"), ARREARS_EXIT(3,\"欠费离场\"), ARREARS_CHARGE(4,\"欠费补缴\"),MANUAL_WIPE_ARREARS(5,\"欠费抹账\");")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "carNumber", value = "车牌号",  dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "bizStatus", value = "业务状态",  dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间",  dataType = "date", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间",  dataType = "date", paramType = "form"),

    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, VehicleEntranceRecord ver){
        Page<VehicleEntranceRecord> page = vehicleEntranceRecordService.fuzzyFindPage(ver,new Page<>(pageRequest.getPageNumber(),pageRequest.getPageSize(),"createTime",false));
        return ResultMsg.resultSuccess(page);
    }



}
