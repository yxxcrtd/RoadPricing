package com.igoosd.rpss.api;

import com.igoosd.common.model.ResultMsg;
import com.igoosd.rpss.service.PrintService;
import com.igoosd.rpss.vo.PrintInfo;
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
 * 2018/4/18.
 */
@Api(tags = "打印接口 enter(verId)  exit(verId) /order(orderId)   ")
@RestController
@RequestMapping("/api/print")
public class PrintController {

    @Autowired
    private PrintService printService;

    @ApiOperation(value = "入场小票", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vehicleEntranceRecordId", required = true, dataType = "long", value = "停车记录ID", paramType = "form")
    })
    @PostMapping("/enter")
    public ResultMsg printEnter(Long vehicleEntranceRecordId){
        List<PrintInfo> list = printService.getEnterReceipt(vehicleEntranceRecordId);
        return ResultMsg.resultSuccess(list);
    }
    @ApiOperation(value = "出场小票", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vehicleEntranceRecordId", required = true, dataType = "long", value = "停车记录ID", paramType = "form")
    })
    @PostMapping("/exit")
    public ResultMsg printExit(Long vehicleEntranceRecordId){
        List<PrintInfo> list = printService.getExitReceipt(vehicleEntranceRecordId);
        return ResultMsg.resultSuccess(list);
    }


    @ApiOperation(value = "订单出票", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", required = true, dataType = "long", value = "订单ID", paramType = "form")
    })
    @PostMapping("/order")
    public ResultMsg printOrder(Long orderId){
        List<PrintInfo> list = printService.printByOrderId(orderId);
        return ResultMsg.resultSuccess(list);
    }
}
