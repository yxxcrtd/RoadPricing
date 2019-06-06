package com.igoosd.rps.controller.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.AccountBalance;
import com.igoosd.rps.service.AccountBalanceService;
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
@RequestMapping("/finance/balance")
@Api(tags = "财务管理-对账管理")
public class AccountBalanceController {


    @Autowired
    private AccountBalanceService accountBalanceService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "对账日期-开始日期（yyyy-MM-dd）", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "对账日期-结束日期（yyyy-MM-dd）", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest,AccountBalance accountBalance){

        Page<AccountBalance> page = accountBalanceService.fuzzyFindPage(accountBalance,new Page<>(pageRequest.getPageNumber(),pageRequest.getPageSize(),"balance_date",false));
        return ResultMsg.resultSuccess(page);
    }
}
