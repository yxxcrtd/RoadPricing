package com.igoosd.rps.controller.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.Order;
import com.igoosd.model.TOrder;
import com.igoosd.rps.service.OrderService;
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
@RestController
@RequestMapping("/finance/order")
@Api(tags = "财务管理-订单记录")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "collectorId", value = "收费员", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest,Order order){
        Page<TOrder> page = orderService.fuzzyFindPage(order,new Page(pageRequest.getPageNumber(),pageRequest.getPageSize(),"createTime",false));
        return ResultMsg.resultSuccess(page);
    }

}
