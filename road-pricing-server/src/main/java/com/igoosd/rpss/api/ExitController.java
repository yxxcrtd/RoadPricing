package com.igoosd.rpss.api;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.ParkingSpaceStatusEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.model.TCollector;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.service.CollectorService;
import com.igoosd.rpss.service.VehicleEntranceRecordService;
import com.igoosd.rpss.service.VerPictureService;
import com.igoosd.rpss.vo.CarInfo;
import com.igoosd.rpss.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/2/26.
 * 出场API
 */
@Api(tags = "出场相关接口")
@RestController
@RequestMapping("/api/exit")
@Slf4j
public class ExitController {

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ChargeRuleService chargeRuleService;
    @Autowired
    private VehicleEntranceRecordService vehicleEntranceRecordService;
    @Autowired
    private VerPictureService verPictureService;
    @Autowired
    private CollectorService collectorService;


    /**
     * @param parkingSpaceId
     * @return
     */
    @ApiOperation("查询预出场车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingSpaceId", value = "车位ID", required = true, dataType = "long", paramType = "form")
    })

    @PostMapping("/carInfo")
    public ResultMsg<CarInfo> getCarInfo(Long parkingSpaceId) {

        Assert.isTrue(LoginUser.get().getParkingSpaceIds().contains(parkingSpaceId),"非法操作...不是所属用户关联的车位信息");

        ParkingSpace ps = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        Assert.notNull(ps, "找不到指定停车位信息");
        Assert.notNull(ps.getVehicleEntranceRecordId(), "当前车位不存在入场车辆信息");
        Date preExitTime = new Date();
        LoginUser user = LoginUser.get();
        BigDecimal chargeAmount = chargeRuleService.getChargeAmount(LoginUser.get().getParkingId(), ps.getEnterTime(), preExitTime);
        BigDecimal realChargeAmount = chargeRuleService.getRealChargeAmount(user.getParkingId(),user.getSubParkingId(), ps.getEnterTime(), preExitTime, ps.getCarNumber());
        TVehicleEntranceRecord record = vehicleEntranceRecordService.getVerById(ps.getVehicleEntranceRecordId());
        TCollector collector = collectorService.getCollectorById(record.getEnterCollectorId());
        CarInfo carInfo = new CarInfo();
        carInfo.setMemberTypeId(ps.getMemberTypeId());
        carInfo.setMemberTypeName(carInfo.getMemberTypeId() == null ? "临时停车" : ps.getMemberTypeName());
        carInfo.setStartDate(ps.getMemberStartDate());
        carInfo.setEndDate(ps.getMemberEndDate());
        carInfo.setTotalArrearsAmount(ps.getTotalArrearsAmount());
        carInfo.setCarNumber(ps.getCarNumber());
        carInfo.setPreExitTime(preExitTime);
        carInfo.setEnterTime(ps.getEnterTime());
        carInfo.setChargeAmount(chargeAmount);
        carInfo.setRealChargeAmount(realChargeAmount);
        carInfo.setTotalArrearsAmount(ps.getTotalArrearsAmount());
        carInfo.setEnterCollectorId(collector.getId());
        carInfo.setEnterColJobNumber(collector.getJobNumber());
        carInfo.setExitCollectorId(user.getCollectorId());
        carInfo.setExitColJobNumber(user.getJobNumber());
        carInfo.setVehicleEntranceRecordId(ps.getVehicleEntranceRecordId());
        //缓存
        redisCacheService.setPreExitCarInfo(carInfo, ps.getVehicleEntranceRecordId());

        //照片url
        List<String> picLists = verPictureService.getDetailPictureUrlsForVerId(ps.getVehicleEntranceRecordId());
        carInfo.setPictureUrls(picLists);
        return ResultMsg.resultSuccess(carInfo);
    }


    /**
     * 缴费出场接口
     * 预判是否为（免费停车时段或者会员抵扣）0元订单、如果是则直接完成出场动作、无需进入下一步操作。
     *
     * @return 出入场记录状态 具体查看 bizStatusEnum 客户端基于状态值进行相应页面显示
     */
    @ApiOperation("缴费出场")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingSpaceId", value = "车位ID", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "isPayingArrears", value = "是否聚合支付", required = true, dataType = "boolean", paramType = "form")
    })
    @PostMapping("/confirmExit")
    public ResultMsg confirmExit(Long parkingSpaceId, boolean isPayingArrears) {

        Assert.isTrue(LoginUser.get().getParkingSpaceIds().contains(parkingSpaceId),"非法操作...不是所属用户关联的车位信息");

        ParkingSpace ps = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        Assert.isTrue(ps.getStatus() == ParkingSpaceStatusEnum.USING.getValue(), "空闲车位不允许此操作");
        CarInfo carInfo = redisCacheService.getPreExitCarInfo(ps.getVehicleEntranceRecordId());
        if (null == carInfo) {
            log.warn("指定的车位预离场信息超时,员工：" + LoginUser.get().getCollectorName());
            //重新预出场操作
            carInfo = getCarInfo(parkingSpaceId).getData();
        }

        BigDecimal totalAmount = carInfo.getRealChargeAmount();
        if (isPayingArrears) {
            totalAmount = totalAmount.add(carInfo.getTotalArrearsAmount() == null ? BigDecimal.ZERO : carInfo.getTotalArrearsAmount());
        }
        boolean flag = totalAmount.compareTo(BigDecimal.ZERO) == 0 ? true : false;
        if (flag) {
            vehicleEntranceRecordService.freeExit(parkingSpaceId, ps.getVehicleEntranceRecordId(), ps.getParkingId(),carInfo.getPreExitTime(),carInfo.getChargeAmount());
        }
        //返回
        Map<String, Object> rstMap = new HashMap<>(1);
        rstMap.put("isFreeExit", flag);
        rstMap.put("vehicleEntranceRecordId",ps.getVehicleEntranceRecordId());
        return ResultMsg.resultSuccess(rstMap);
    }

    /**
     * 欠费离场
     *
     * @return
     */
    @ApiOperation("欠费离场")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingSpaceId", value = "车位ID", required = true, dataType = "long", paramType = "form")
    })
    @PostMapping("/arrearsExit")
    public ResultMsg arrearsExit(Long parkingSpaceId) {

        Assert.isTrue(LoginUser.get().getParkingSpaceIds().contains(parkingSpaceId),"非法操作...不是所属用户关联的车位信息");

        ParkingSpace ps = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        if (ps.getVehicleEntranceRecordId() == null) {
            return ResultMsg.resultFail("当前车位没有车辆驶入");
        }
        CarInfo carInfo = redisCacheService.getPreExitCarInfo(ps.getVehicleEntranceRecordId());
        if(null == carInfo){
            carInfo = getCarInfo(ps.getId()).getData();
        }
        if(carInfo.getRealChargeAmount().compareTo(BigDecimal.ZERO) == 0){
            return ResultMsg.resultFail("实际缴费为0元，无法进行欠费离场操作操作");
        }
        vehicleEntranceRecordService.arrearsExit(ps.getVehicleEntranceRecordId(),carInfo.getChargeAmount(),carInfo.getRealChargeAmount(),carInfo.getPreExitTime());

        return ResultMsg.resultSuccess();
    }


    /**
     * 上传入场照片
     *
     * @param file                    上传的文件
     * @param parkingSpaceId 停车记录ID
     * @return
     */
    @ApiOperation("上传车辆照片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parkingSpaceId", value = "车位ID", dataType = "Long", required = true,paramType = "form")
    })
    @PostMapping(value = "/upload",consumes = "multipart/*",headers = "Content-Type=multipart/form-data")
    public ResultMsg uploadPicture(@ApiParam(value = "上传照片")@RequestParam("file") MultipartFile file, @RequestParam Long parkingSpaceId) throws IOException {

        //Assert.isTrue(LoginUser.get().getParkingSpaceIds().contains(parkingSpaceId),"非法操作...不是所属用户关联的车位信息");

        ParkingSpace ps = redisCacheService.getParkingSpaceInfo(parkingSpaceId);
        if(null == ps){
            return ResultMsg.resultSuccess("指定的车位不合法");
        }
        Long verId = ps.getVehicleEntranceRecordId();
        if(null == verId){
            return ResultMsg.resultFail("指定的车位没有停车信息");
        }
        String picUrl = verPictureService.uploadPicture(verId,file.getInputStream());
        return ResultMsg.resultSuccess("",picUrl);
    }

}
