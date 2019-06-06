package com.igoosd.rps.controller.main;

import com.igoosd.common.model.ResultMsg;
import com.igoosd.model.TAccountBalance;
import com.igoosd.rps.service.AccountBalanceService;
import com.igoosd.rps.service.OrderService;
import com.igoosd.rps.service.ParkingSpaceService;
import com.igoosd.rps.vo.Income;
import com.igoosd.rps.vo.IncomeSummary;
import com.igoosd.rps.vo.ParkingSpaceRatio;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 2018/5/14.
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountBalanceService accountBalanceService;

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @ApiOperation(value = "查询当前车位使用率", notes = "")
    @ApiImplicitParams({
    })
    @PostMapping("/parkingSpaceRatio")
    public ResultMsg getParkinbgSpaceRatio() {
        ParkingSpaceRatio ratio = parkingSpaceService.getParkinbgSpaceRatio();
        return ResultMsg.resultSuccess(ratio);
    }

    @ApiOperation(value = "收入汇总", notes = "")
    @ApiImplicitParams({
    })
    @PostMapping("/incomeSummary")
    public ResultMsg getIncomeSummary() {

        BigDecimal onlineIncomeForCurDay = orderService.getTotalAmountForCurDay(true);
        BigDecimal offlineIncomeForCurDay = orderService.getTotalAmountForCurDay(false);

        onlineIncomeForCurDay = null == onlineIncomeForCurDay ? BigDecimal.ZERO : onlineIncomeForCurDay;
        offlineIncomeForCurDay = null == offlineIncomeForCurDay ? BigDecimal.ZERO : offlineIncomeForCurDay;

        List<TAccountBalance> list = accountBalanceService.findListByCurMonth();

        BigDecimal monthOnlineAmount = BigDecimal.ZERO;
        BigDecimal monthOfflineAmount = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(list)) {
            for (TAccountBalance accountBalance : list) {
                monthOnlineAmount = monthOnlineAmount.add(accountBalance.getTotalOnlineAmount());
                monthOfflineAmount = monthOfflineAmount.add(accountBalance.getTotalOfflineAmount());
            }
        }
        IncomeSummary incomeSummary = new IncomeSummary();
        incomeSummary.setOnlineIncomeForCurDay(onlineIncomeForCurDay);
        incomeSummary.setOfflineIncomeForCurDay(offlineIncomeForCurDay);
        incomeSummary.setTotalIncomeForCurDay(incomeSummary.getOnlineIncomeForCurDay().add(incomeSummary.getOfflineIncomeForCurDay()));

        incomeSummary.setOfflineIncomeForCurMonth(monthOfflineAmount.add(incomeSummary.getOfflineIncomeForCurDay()));
        incomeSummary.setOnlineIncomeForCurMonth(monthOnlineAmount.add(incomeSummary.getOnlineIncomeForCurDay()));
        incomeSummary.setTotalIncomeForCurMonth(incomeSummary.getOfflineIncomeForCurMonth().add(incomeSummary.getOnlineIncomeForCurMonth()));
        return ResultMsg.resultSuccess(incomeSummary);
    }

    @ApiOperation(value = "totalAmountList", notes = "查询方式 ： 0： 默认 按日  1：按月，其中按日查询近七天的柱状图，按月 查询近十二个月柱状图，界面同时存在柱状图和折线图下拉框 进行当前数据的图表实时切换")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryWay", value = "查询方式", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/totalAmountList")
    public ResultMsg getTotalAmountList(int queryWay) {
        //按日查询
        List<Income> incomeList = accountBalanceService.getIncomeListByQueryWay(queryWay);
        return ResultMsg.resultSuccess(incomeList);
    }

}
