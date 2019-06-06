package com.igoosd.rps.controller.duty;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.DutyStatistics;
import com.igoosd.rps.service.DutyStatisticsService;
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
@Api(tags = "考勤管理-考勤统计")
@RestController
@RequestMapping("/duty/statistics")
public class DutyStatisticsController {

    @Autowired
    private DutyStatisticsService dutyStatisticsService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段", dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "collectorId", value = "收费员", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "考勤日期-开始日期（yyyy-MM-dd）", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "考勤日期-结束日期（yyyy-MM-dd）", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, DutyStatistics dutyStatistics){

       Page<DutyStatistics> page = dutyStatisticsService.fuzzyFindPage(dutyStatistics,new Page<>(pageRequest.getPageNumber(),pageRequest.getPageSize(),"createTime",false));

       return ResultMsg.resultSuccess(page);

    }
}
