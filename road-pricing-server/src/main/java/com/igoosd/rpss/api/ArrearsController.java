package com.igoosd.rpss.api;

import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.vo.ArrearsDetailInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 2018/2/27.
 * 欠费补缴接口 pos机
 */
@Api(tags = "欠费补缴模块")
@RestController
@RequestMapping("/api/arrears")
public class ArrearsController {

    @Autowired
    private ArrearsService arrearsService;

    @ApiOperation(value = "查询指定车牌的欠费记录列表", notes = "查询指定车牌的欠费记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true, dataType = "string", paramType = "form"),
    })
    @PostMapping("/queryList")
    public ResultMsg queryArrearsRecordList(String carNumber) {
        List<ArrearsRecord> list = arrearsService.findArrearsListByCarNumber(carNumber);
        return ResultMsg.resultSuccess(list);
    }

    @ApiOperation(value = "欠费详情", notes = "根据欠费ID查找欠费详情")
    @ApiImplicitParam(name = "id", value = "欠费详情ID", required = true, dataType = "long", paramType = "form")
    @PostMapping("/detailInfo")
    public ResultMsg getArrearsDetailInfo(Long id) {
        ArrearsDetailInfo detailInfo = arrearsService.getArrearsDetailById(id);
        return ResultMsg.resultSuccess(detailInfo);
    }

}
