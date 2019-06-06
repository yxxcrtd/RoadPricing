package com.igoosd.rpss.api;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.Order;
import com.igoosd.model.TAccountSummary;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.service.AccountSummaryService;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.service.OrderService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import com.igoosd.rpss.util.StringUtils;
import com.igoosd.rpss.vo.BusinessInfo;
import com.igoosd.rpss.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/2/27.
 */
@Slf4j
@RequestMapping("/api/pay")
@RestController
@Api(tags = "支付相关接口")
public class PaymentController {


    @Autowired
    private OrderService orderService;
    @Autowired
    private ArrearsService arrearsService;
    @Autowired
    private AccountSummaryService accountSummaryService;
    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;


    /**
     * @param orderType               订单类型
     * @param vehicleEntranceRecordId 停车记录ID
     * @param arrearsRecordIds        欠费记录ID
     * @return
     */
    @ApiOperation(value = "预下单接口", notes = "订单类型： 1：缴费订单（vehicleEntranceRecordId）  2：欠费补缴订单（arrearsRecordIds） 3：聚合订单（vehicleEntranceRecordId） 4：扎帐订单 支付方式：1:wechat 2alipay3 银行卡 4 现金  ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderType", required = true, dataType = "int", value = "订单类型", paramType = "form"),
            @ApiImplicitParam(name = "payWay", required = true, dataType = "int", value = "支付方式", paramType = "form"),
            @ApiImplicitParam(name = "vehicleEntranceRecordId", dataType = "long", value = "停车记录ID", paramType = "form"),
            @ApiImplicitParam(name = "arrearsRecordIds", dataType = "string", value = "欠费记录ID集合", paramType = "form"),
            @ApiImplicitParam(name = "accountSummaryId", dataType = "long", value = "扎帐记录ID", paramType = "form")

    })
    @PostMapping("/prePay")
    public ResultMsg preOrder(@RequestParam Integer orderType, @RequestParam Integer payWay,
                              Long vehicleEntranceRecordId, String arrearsRecordIds) {

        PayWayEnum payWayEnum = PayWayEnum.getPayWayEnum(payWay);
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderType);
        if (orderTypeEnum == null) {
            return ResultMsg.resultFail("不合法的订单类型");
        }
        if (payWayEnum == null) {
            return ResultMsg.resultFail("不合法的支付方式");
        }
        if (PayWayEnum.ALI_PAY != payWayEnum && PayWayEnum.WECHAT_PAY != payWayEnum) {
            return ResultMsg.resultFail("不合法的支付方式");
        }

        try {
            BusinessInfo businessInfo = getBusinessInfo(orderTypeEnum, vehicleEntranceRecordId, arrearsRecordIds);
            //预下单
            Order order = orderService.preOrder(orderType, payWay, businessInfo);
            Map<String, Object> rstMap = new HashMap<>(3);
            rstMap.put("orderId", order.getId());
            rstMap.put("payWay", order.getPayWay());
            rstMap.put("qrCodeUrl", order.getQrCodeUrl());
            return ResultMsg.resultSuccess(rstMap);

        } catch (RoadPricingException e) {
            return ResultMsg.resultFail(e.getMessage());
        }

    }

    @ApiOperation(value = "轮询订单确认接口", notes = "payStatus: 1:待支付 2：支付成功 3：支付失败,4:支付取消，5：处理中 6:超时")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", required = true, dataType = "Long", value = "订单id", paramType = "form")
    })
    @PostMapping("/confirm")
    public ResultMsg orderConfirm(Long orderId) {
        PayStatusEnum payStatusEnum = orderService.getOrderPayStatus(orderId);
        Map<String, Integer> map = new HashMap<>(1);
        map.put("payStatus", payStatusEnum.getValue());
        return ResultMsg.resultSuccess(payStatusEnum.getName(), map);
    }


    @ApiOperation(value = "现金支付", notes = "现金支付只有支付成功状态  或者-200 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderType", required = true, dataType = "int", value = "订单类型", paramType = "form"),
            @ApiImplicitParam(name = "vehicleEntranceRecordId", dataType = "long", value = "停车记录ID", paramType = "form"),
            @ApiImplicitParam(name = "arrearsRecordIds", dataType = "string", value = "欠费记录ID集合 逗号分隔", paramType = "form")
    })
    @PostMapping("/cashPay")
    public ResultMsg cashPay(@RequestParam Integer orderType, Long vehicleEntranceRecordId, String arrearsRecordIds) {

        LoginUser user = LoginUser.get();
        OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(orderType);
        if (orderTypeEnum == null || orderTypeEnum == OrderTypeEnum.PITCH_ACCOUNT_CHARGE_ORDER) {
            return ResultMsg.resultFail("不合法的订单类型");
        }
        try {
            BusinessInfo businessInfo = getBusinessInfo(orderTypeEnum, vehicleEntranceRecordId, arrearsRecordIds);
            Long orderId = orderService.payCash(businessInfo, orderTypeEnum);
            Map<String, Long> rstMap = new HashMap<>(1);
            rstMap.put("orderId", orderId);
            return ResultMsg.resultSuccess(rstMap);
        } catch (RoadPricingException e) {
            return ResultMsg.resultFail(e.getMessage());
        }
    }

    private BusinessInfo getBusinessInfo(OrderTypeEnum orderTypeEnum, Long vehicleEntranceRecordId, String arrearsRecordIds) throws RoadPricingException {
        List<Long> arrearsRecordIdList = null;
        Long accountSummaryId = null;
        switch (orderTypeEnum) {
            case ARREARS_CHARGE_ORDER: {
                Assert.isTrue(!StringUtils.isEmpty(arrearsRecordIds), "{}欠费记录id集合不能为空", orderTypeEnum.getName());
                arrearsRecordIdList = StringUtils.commaDelimitedListToLongList(arrearsRecordIds);
                break;
            }
            case PITCH_ACCOUNT_CHARGE_ORDER: {
                Long collectorId = LoginUser.get().getCollectorId();
                TAccountSummary tas = accountSummaryService.getLatestAccountSummaryRecordByCollectorId(collectorId);
                if ((tas.getSummaryStatus() & SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue()) == SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue()) {
                    throw new RoadPricingException("收费员当日已扎帐...");
                }
                accountSummaryId = tas.getId();
                break;
            }
            case MERGE_CHARGE_ORDER: {
                Assert.notNull(vehicleEntranceRecordId, "{}停车记录Id不能为空", orderTypeEnum.getName());
                //查找欠费记录ID集合
                TVehicleEntranceRecord record = vehicleEntranceRecordService.getVerById(vehicleEntranceRecordId);
                List<Long> list = arrearsService.findArrearsIdsByCarNumber(record.getCarNumber());
                Assert.isTrue(!CollectionUtils.isEmpty(list), "{}没有关联的欠费记录", orderTypeEnum.getName());
                arrearsRecordIdList = list;
            }
            case PARKING_CHARGE_ORDER: {
                Assert.notNull(vehicleEntranceRecordId, "{}停车记录Id不能为空", orderTypeEnum.getName());
                break;
            }
        }
        BusinessInfo businessInfo = new BusinessInfo();
        businessInfo.setVehicleEntranceRecordId(vehicleEntranceRecordId);
        businessInfo.setArrearsRecordIds(arrearsRecordIdList);
        businessInfo.setAccountSummaryId(accountSummaryId);
        return businessInfo;
    }

}
