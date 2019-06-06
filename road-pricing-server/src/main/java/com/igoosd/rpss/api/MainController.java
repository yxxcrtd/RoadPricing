package com.igoosd.rpss.api;

import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.GeoMapUtils;
import com.igoosd.domain.LocationReport;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.service.LocationReportService;
import com.igoosd.rpss.util.RpssConst;
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

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 2018/2/7.
 */
@Api(tags = "首页接口")
@RestController
@RequestMapping("/api/main")
public class MainController {


    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private LocationReportService locationReportService;

    @ApiOperation("查询当前路段车位列表")
    @PostMapping("/spsList")
        public ResultMsg findAllParkingSpace() {
        Collection subParkingId = LoginUser.get().getParkingSpaceIds();
        List<ParkingSpace> list = redisCacheService.multiGetParkingSpace(subParkingId);
        return ResultMsg.resultSuccess(list);
    }

    @ApiOperation("考勤位置上报")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "location", value = "位置(纬度,经度)", required = true, dataType = "string", paramType = "form"),
    })
    @PostMapping("/reportLocation")
    public ResultMsg reportLocation(@RequestParam String location){
        LoginUser user = LoginUser.get();
        LocationReport lr = new LocationReport();
        lr.setCollectorId(user.getCollectorId());
        lr.setDeviceId(user.getDeviceId());
        lr.setReportTime(new Date());
        lr.setLocation(location);
        lr.setAllow(true);
        //地理位置校验
        Double distance = GeoMapUtils.distance(user.getSubParkingLocation(),location);
        lr.setDistance(distance.intValue());
        //在配置范围内
        if(Double.parseDouble(configService.getValue(RpssConst.cfg_key_login_range)) < distance){
            lr.setAllow(false);
        }
        locationReportService.reportLocation(lr);
        return ResultMsg.resultSuccess();
    }
}
