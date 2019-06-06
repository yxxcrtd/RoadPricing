package com.igoosd.rps.controller.duty;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.DutyRecord;
import com.igoosd.rps.service.DutyRecordService;
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
@Api(tags = "考勤管理-执勤记录")
@RestController
@RequestMapping("/duty/record")
public class DutyRecordController {

    @Autowired
    private DutyRecordService dutyRecordService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "collectorId", value = "收费员", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间（yyyy-MM-dd HH:mm:ss）", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "开始时间（yyyy-MM-dd HH:mm:ss）", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, DutyRecord dutyRecord) {

        Page<DutyRecord> page = dutyRecordService.fuzzyFindPage(dutyRecord, new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize(), "loginTime", false));
        return ResultMsg.resultSuccess(page);
    }
}
