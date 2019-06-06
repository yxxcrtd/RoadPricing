package com.igoosd.rps.controller.operation;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.rps.service.ArrearsRecordService;
import com.igoosd.rps.service.VehicleEntranceRecordService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2018/5/10.
 */
@Api(tags = "运营管理-欠费记录")
@RestController
@RequestMapping("/operation/arrears")
public class ArrearsController {

    @Autowired
    private ArrearsRecordService arrearsRecordService;
    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "carNumber", value = "车牌号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("page")
    public ResultMsg page( PageRequest pageRequest, ArrearsRecord arrearsRecord) {
        Page<ArrearsRecord> page = arrearsRecordService.fuzzyFindPage(arrearsRecord, new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize(), "createTime", false));
        return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "抹账（注意二次提醒）", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/wipe")
    public ResultMsg wipeArrears(@RequestParam Long id) {
        arrearsRecordService.wipeArrears(id);
        return ResultMsg.resultSuccess("抹账成功");
    }

    @ApiOperation(value = "查看详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vehicleEntranceRecordId", value = "停车记录ID", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/detail")
    public ResultMsg getArrearsDetails(@RequestParam Long vehicleEntranceRecordId) {
        VehicleEntranceRecord ver = vehicleEntranceRecordService.getDetailVerByKey(vehicleEntranceRecordId);
        return ResultMsg.resultSuccess(ver);
    }

}
