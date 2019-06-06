package com.igoosd.rpss.api;

import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.Order;
import com.igoosd.rpss.service.CollectorService;
import com.igoosd.rpss.service.OrderService;
import com.igoosd.rpss.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 2018/2/27.
 */
@Api(tags = "收费员（我的）相关接口")
@RestController
@RequestMapping("/api/collector")
public class CollectorController {

    @Autowired
    private CollectorService collectorService;

    @Autowired
    private OrderService orderService;


    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "旧密码", dataType = "string", required = true, paramType = "form"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", dataType = "string", required = true, paramType = "form")
    })
    @PostMapping("/modifyPwd")
    public ResultMsg modifyPwd(String oldPwd, String newPwd) {
        collectorService.modifyPwd(oldPwd, newPwd);
        return ResultMsg.resultSuccess();
    }


    /**
     * @return
     */
    @ApiOperation("查询当前收费情况统计")
    @PostMapping("/totalChargeInfo")
    public ResultMsg queryTotalChargeInfo() {
        //获取当日总收入
        Order order = new Order();
        order.setStartTime(DateUtil.convertYmdDate(new Date()));
        order.setEndTime(DateUtil.getNextDate(order.getStartTime()));
        order.setPayStatus(PayStatusEnum.PAY_SUCCESS.getValue());
        order.setCollectorId(LoginUser.get().getCollectorId());
        BigDecimal totalAmount = orderService.getOrderAmount(order);
        totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
        //线下
        order.setPayWay(PayWayEnum.CASH_PAY.getValue());
        BigDecimal offlineAmount = orderService.getOrderAmount(order);
        offlineAmount = offlineAmount != null ? orderService.getOrderAmount(order) : BigDecimal.ZERO;
        //线上
        BigDecimal onlineAmount = totalAmount.subtract(offlineAmount);
        Map<String, BigDecimal> map = new HashMap<>(3);
        map.put("totalAmount", totalAmount);
        map.put("offlineAmount", offlineAmount);
        map.put("onlineAmount", onlineAmount);
        return ResultMsg.resultSuccess(map);
    }

}
