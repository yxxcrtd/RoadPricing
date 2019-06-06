package com.igoosd.rpss.api;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.Const;
import com.igoosd.common.util.DateUtil;
import com.igoosd.common.util.GeoMapUtils;
import com.igoosd.common.util.HashKit;
import com.igoosd.common.util.RSACryptography;
import com.igoosd.domain.SubParking;
import com.igoosd.model.TCollector;
import com.igoosd.model.TDevicePos;
import com.igoosd.rpss.service.AccountSummaryService;
import com.igoosd.rpss.service.CollectorService;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.service.DevicePosService;
import com.igoosd.rpss.service.DutyService;
import com.igoosd.rpss.service.ParkingSpaceService;
import com.igoosd.rpss.service.SubParkingService;
import com.igoosd.rpss.util.RpssConst;
import com.igoosd.rpss.util.StringUtils;
import com.igoosd.rpss.util.WebUtils;
import com.igoosd.rpss.vo.LoginInfo;
import com.igoosd.rpss.vo.LoginUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/2/5.
 */
@RestController
@RequestMapping("/common")
@Slf4j
@Api(tags = "登录登出接口")
public class CommonController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CollectorService collectorService;
    @Autowired
    private DevicePosService devicePosService;
    @Autowired
    private SubParkingService subParkingService;
    @Autowired
    private ParkingSpaceService  parkingSpaceService;
    @Autowired
    private AccountSummaryService accountSummaryService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private ConfigService configService;

    @Value("${igoosd.server.private-key}")
    private String privateKey;
    @Value("${igoosd.server.public-key}")
    private String publicKey;


    /**
     * RSA 通用获取 为客户端传递 用户名密码 加密服务
     *
     * @return
     */
    @PostMapping("/rsa")
    public ResultMsg getRSA() {
        String[] keys = RSACryptography.genPublicAndPrivateKeys(1024);
        log.info("publicKey:{}\nprivateKey:{}", keys[0], keys[1]);
        Map<String, String> map = new HashMap<>(1);
        map.put(Const.PUBLIC_KEY, publicKey);
        return ResultMsg.resultSuccess(map);
    }


    /**
     * 登录拒绝：
     * 1.数据异常拒绝登录
     * 2.用户名密码错误拒绝登录
     * 3.参数异常拒绝登录
     * 4.设备-收费员未绑定 拒绝登录
     * 5.已扎帐 拒绝登录
     * 6.22:00以后 拒绝登录
     *
     * @param loginInfo
     * @return
     */
    @ApiOperation(value = "登录接口", notes = "明文格式：deviceId&job_number&password 如：daasda1232313asdasd-1212&AH001&123456")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cipherText", value = "加密的密文", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "publicKey", value = "公钥", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "location", value = "位置,'经度,纬度' 字符串", required = true,dataType = "string", paramType = "form")
    })
    @PostMapping("/login")
    public ResultMsg login(HttpServletRequest request,LoginInfo loginInfo) {

        log.info(JSON.toJSONString(loginInfo));
        Assert.hasText(loginInfo.getCipherText(), "cipherText 不能为空");
        Assert.hasText(loginInfo.getPublicKey(), "publicKey 不能为空");
        Assert.hasText(loginInfo.getLocation(),"location不能为空");

        //22点后不允许登录
        int refuseTime = Integer.parseInt(configService.getValue(RpssConst.cfg_key_login_hour));
        if(DateUtil.getDateNumber(new Date(), Calendar.HOUR_OF_DAY)>=refuseTime){
            return ResultMsg.resultFail(-200,"当日"+refuseTime+":00后不允许登录");
        }

        String[] plainText = RSACryptography.decryptByprivateKey(privateKey, loginInfo.getCipherText()).split(Const.PLAIN_TEXT_SPLIT);
        if (plainText.length != 3) {
            return ResultMsg.resultFail(-200, "不符合规范的密文");
        }
        String deviceId = plainText[0];
        String jobNumber = plainText[1];
        String password = plainText[2];

        //身份验证
        TCollector collector = collectorService.loginVerify(jobNumber, password);
        if (null == collector) {
            return ResultMsg.resultFail(-200, "无效的用户名或密码");
        }
        //deviceId 绑定校验
        TDevicePos tdp = devicePosService.getTDevicePosByDeviceId(deviceId);
        if (null == tdp) {
            return ResultMsg.resultFail(-200, "设备未初始化或初始化异常，请联系管理员");
        }
        SubParking subParking = subParkingService.getSubParkingByKey(tdp.getSubParkingId());
        if (null == subParking) {
            return ResultMsg.resultFail(-200, "无效的数据，请联系管理员");
        }
        if(Boolean.parseBoolean(configService.getValue(RpssConst.cfg_key_login_range_verify))){
            //地理位置校验
            String spLocation = subParking.getGeoLocation();
            double distance = GeoMapUtils.distance(loginInfo.getLocation(),spLocation);
            //300米范围内
            if(Double.parseDouble(configService.getValue(RpssConst.cfg_key_login_range)) < distance){
                return ResultMsg.resultFail(-200,"超出登录范围,拒绝登录");
            }
        }
        //扎帐校验
        boolean isAllow = accountSummaryService.allowLoginForAccountSummary(collector.getId());
        if (!isAllow) {
            return ResultMsg.resultFail(-200, "当日已扎帐,不允许登录");
        }
        List<Long> parkingSpaceIds =parkingSpaceService.findIdsBySubParkingId(subParking.getId());
        if(CollectionUtils.isEmpty(parkingSpaceIds)){
            return ResultMsg.resultFail(-200,"数据异常，绑定的路段未添加车位");
        }
        ///添加考勤记录
        Long dutyId = dutyService.saveLoginDutyRecord(collector.getId(),subParking.getId(),loginInfo.getLocation());
        //生成SessionId 和token
        LoginUser user = new LoginUser();
        user.setDutyRecordId(dutyId);// 考勤记录ID
        user.setCollectorId(collector.getId());
        user.setCollectorName(collector.getName());
        user.setPhone(collector.getPhone());
        user.setJobNumber(collector.getJobNumber());
        user.setParkingId(subParking.getParkingId());
        user.setParkingName(subParking.getParkingName());
        user.setSubParkingId(subParking.getId());
        user.setLoginDate(new Date());
        user.setParkingAddress(subParking.getParkingAddress());
        user.setParkingSpaceIds(parkingSpaceIds);
        user.setSubParkingLocation(subParking.getGeoLocation());
        user.setIp(WebUtils.getIP(request));
        user.setDeviceId(deviceId);
        String userInfo = JSON.toJSONString(user);
        log.info("用户信息:{}", userInfo);
        //
        String sessionKey = HashKit.md5(userInfo);
        String token = HashKit.sha1(userInfo);

        String redisKey = Const.REDIS_TOKEN_PRE_KEY + sessionKey;
        redisTemplate.boundHashOps(redisKey).put(Const.SESSION_USER, userInfo);
        redisTemplate.boundHashOps(redisKey).put(Const.SESSION_TOKEN, token);
        String oldSessionId = redisTemplate.opsForValue().get(Const.REDIS_SESSION_USER_PRE_KEY+collector.getId());
        if(!StringUtils.isEmpty(oldSessionId)){
            //TODO 上次登录不是同一台设备 发推送
           //TODO Map map = redisTemplate.boundHashOps((Const.REDIS_TOKEN_PRE_KEY + oldSessionId)).entries();
            //删除旧的session
            redisTemplate.delete((Const.REDIS_TOKEN_PRE_KEY + oldSessionId));
        }
        //更新新的session到用户关联中去
        redisTemplate.opsForValue().set(Const.REDIS_SESSION_USER_PRE_KEY+collector.getId(),sessionKey);

        Map<String, Object> resultData = new HashMap<>();
        String origData = sessionKey + Const.PLAIN_TEXT_SPLIT + token;
        log.info("用户:{},登录认证信息:{}",userInfo,origData);
        String cipherText = RSACryptography.encryptBypublicKey(loginInfo.getPublicKey(), origData);
        int reportTimes = Integer.parseInt(configService.getValue(RpssConst.cfg_key_report_times));
        resultData.put("cipherText", cipherText);
        resultData.put("parkingId", user.getParkingId());
        resultData.put("parkingName", user.getParkingName());
        resultData.put("subParkingId", user.getSubParkingId());
        resultData.put("collectorName", user.getCollectorName());
        resultData.put("parkingAddress",user.getParkingAddress());
        resultData.put("reportTimes",reportTimes);
        resultData.put("bluetoothId",tdp.getBluetoothId());
        resultData.put("deviceType",tdp.getDeviceType());

        return ResultMsg.resultSuccess(resultData);
    }


    /**
     * 登出、退出 需要权限认证
     *
     * @param request
     * @return
     */
    @ApiOperation("退出接口")
    @ApiImplicitParams({
    })
    @PostMapping("/logout")
    public ResultMsg logout(HttpServletRequest request) {
        LoginUser user = LoginUser.get();
        dutyService.updateLogoutDutyRecord(user.getDutyRecordId());//更新执勤信息

        String sessionId = request.getHeader(Const.HEADER_SESSION_ID);
        String redisKey = Const.REDIS_TOKEN_PRE_KEY + sessionId;
        redisTemplate.delete(redisKey);
        redisTemplate.delete(Const.REDIS_SESSION_USER_PRE_KEY+LoginUser.get().getCollectorId());
        return ResultMsg.resultSuccess();

    }

}
