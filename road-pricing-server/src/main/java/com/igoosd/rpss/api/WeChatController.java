package com.igoosd.rpss.api;

import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import com.igoosd.rpss.service.VerPictureService;
import com.igoosd.rpss.vo.CarInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/12.
 * 微信接口
 */
@Api(tags = "微信接口")
@RequestMapping("/wechat")
@RestController
public class WeChatController {

    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;

    @Autowired
    private ArrearsService arrearsService;
    @Autowired
    private ChargeRuleService chargeRuleService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private VerPictureService verPictureService;

    @ApiOperation(value = "获取车辆信息（预出场缓存）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", paramType = "form", required = true, dataType = "string"),
    })
    @PostMapping("/carInfo")
    public ResultMsg getParkingCarInfo(@RequestParam String carNumber) {

        CarInfo carInfo = null;
        VehicleEntranceRecord ver = vehicleEntranceRecordService.getCurDateParkingVerInfo(carNumber);
        if (null != ver) {
            Date preExitDate = new Date();
            //查询欠费总计 和应收应缴费用
            BigDecimal totalArrearsAmount = arrearsService.getTotalArrearsAmount(carNumber);
            BigDecimal chargeAmount = chargeRuleService.getChargeAmount(ver.getParkingId(), ver.getEnterTime(), preExitDate);
            BigDecimal realChargeAmount = chargeRuleService.getRealChargeAmount(ver.getParkingId(), ver.getSubParkingId(),ver.getEnterTime(), preExitDate, carNumber);

            carInfo = new CarInfo();
            carInfo.setMemberTypeName(ver.getMemberTypeName());
            carInfo.setPreExitTime(new Date());
            carInfo.setCarNumber(ver.getCarNumber());
            carInfo.setEnterTime(ver.getEnterTime());
            carInfo.setParkingSpaceCode(ver.getParkingSpaceCode());
            carInfo.setParkingName(ver.getParkingName());
            carInfo.setParkingAddress(ver.getParkingAddress());
            carInfo.setStartDate(ver.getMemberStartDate());
            carInfo.setEndDate(ver.getMemberEndDate());
            carInfo.setTotalArrearsAmount(totalArrearsAmount);
            carInfo.setChargeAmount(chargeAmount);
            carInfo.setRealChargeAmount(realChargeAmount);
            //预出场缓存
            redisCacheService.setPreExitCarInfo(carInfo, ver.getId());
            //获取图片信息
            List<String> list  = verPictureService.getDetailPictureUrlsForVerId(ver.getId());
            carInfo.setPictureUrls(list);
            return ResultMsg.resultSuccess(carInfo);
        }

        return ResultMsg.resultSuccess(carInfo);
    }


}
