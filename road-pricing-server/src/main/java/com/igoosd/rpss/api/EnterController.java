package com.igoosd.rpss.api;

import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.RegexUtils;
import com.igoosd.domain.Member;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.service.MemberService;
import com.igoosd.rpss.service.PrintService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import com.igoosd.rpss.vo.CarInfo;
import com.igoosd.rpss.vo.LoginUser;
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
import java.util.HashMap;
import java.util.Map;

/**
 * 2018/2/7.
 * 入场接口
 */
@Api(tags = "入场相关接口")
@RestController
@RequestMapping("api/entrance")
public class EnterController {


    @Autowired
    private ArrearsService arrearsService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;
    @Autowired
    private PrintService printService;


    @ApiOperation("获取车辆信息")
    @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true, dataType = "string",paramType = "form")
    @PostMapping("carInfo")
    public ResultMsg getCarInfo(@RequestParam("carNumber") String carNumber) {

        //欠费信息
        BigDecimal totalArrearsAmount = arrearsService.getTotalArrearsAmount(carNumber);
        //会员标志
        Member member = memberService.getCarNumberMemberType(carNumber, LoginUser.get().getSubParkingId());
        CarInfo carInfo = new CarInfo();
        carInfo.setCarNumber(carNumber);
        carInfo.setTotalArrearsAmount(totalArrearsAmount);
        if (member == null) {
            carInfo.setMemberTypeName("临时停车");
        } else {
            carInfo.setMemberTypeId(member.getMemberTypeId());
            carInfo.setMemberTypeName(member.getMemberTypeName());
            carInfo.setStartDate(member.getStartDate());
            carInfo.setEndDate(member.getEndDate());
        }
        return ResultMsg.resultSuccess(carInfo);
    }

    /**
     * 确认入场
     *
     * @return
     */
    @ApiOperation("确认入场")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", dataType = "string", required = true,paramType = "form"),
            @ApiImplicitParam(name = "parkingSpaceId", value = "车位ID", dataType = "long", required = true,paramType = "form")
    })
    @PostMapping("/confirm")
    public ResultMsg confirmEntrance(@RequestParam Long parkingSpaceId, @RequestParam String carNumber) {
        Assert.isTrue(LoginUser.get().getParkingSpaceIds().contains(parkingSpaceId),"非法操作...不是所属用户关联的车位信息");
        //车牌校验
        Assert.isTrue(RegexUtils.isCarNumber(carNumber)," 请输入准合法的车牌");

        Long verId = vehicleEntranceRecordService.enterCar(carNumber, parkingSpaceId);
        Map<String,Object> rstMap = new HashMap<>(1);
        rstMap.put("vehicleEntranceRecordId",verId);
        return ResultMsg.resultSuccess(rstMap);
    }


}
