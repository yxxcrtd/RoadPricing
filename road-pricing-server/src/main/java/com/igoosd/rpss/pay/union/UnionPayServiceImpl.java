package com.igoosd.rpss.pay.union;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.util.HashKit;
import com.igoosd.rpss.pay.PayService;
import com.igoosd.rpss.pay.union.util.PayChannelEnum;
import com.igoosd.rpss.pay.union.util.ResultStatusEnum;
import com.igoosd.rpss.pay.union.util.SignUtil;
import com.igoosd.rpss.pay.union.util.UnionProperties;
import com.igoosd.rpss.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 2018/3/21.
 */
@Service
@Slf4j
public class UnionPayServiceImpl implements PayService {


    @Autowired
    private UnionProperties unionProperties;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RestTemplate restTemplate;

    private static final ExecutorService pool = Executors.newFixedThreadPool(20);

    @Override
    public Map<String, String> pay(String orderNo, PayWayEnum payWayEnum, BigDecimal realChargeAmount, OrderTypeEnum orderTypeEnum) {

        PayChannelEnum payChannelEnum = PayChannelEnum.getUnionPayChannelEnum(payWayEnum);
        Assert.notNull(payChannelEnum, "暂不支持该支付方式进行支付，支付方式:{}", payWayEnum.getName());

        TreeMap<String, String> params = new TreeMap<>();
        params.put("cusid", unionProperties.getCusid());
        params.put("appid", unionProperties.getAppid().toString());
        params.put("version", unionProperties.getVersion());
        params.put("trxamt", realChargeAmount.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString()); //交易金额 单位分 四舍五入取整
        params.put("reqsn", orderNo);
        params.put("paytype", payChannelEnum.getValue());
        params.put("randomstr", HashKit.getRandStr(8));
        params.put("body", orderTypeEnum.getName());//订单商品名称
        params.put("remark", orderTypeEnum.getName());
        //params.put("acct", acct);//默认15分钟有效时间  以分为单位 最大60分钟
        //params.put("authcode", authcode);
        params.put("notify_url", unionProperties.getNotifyUrl());
        params.put("limit_pay", "no_credit");
        params.put("sign", SignUtil.sign(params, unionProperties.getKey()));
        Map<String, String> rstMap = postreq(unionProperties.getDomain() + "pay",params,"预支付");
        if ("0000".equals(rstMap.get("trxstatus"))) {
            return rstMap;
        }
        throw new RoadPricingException("获取预支付交易对象异常,请重试");
    }


    /**
     * 回调业务处理
     *
     * @param rstMap
     */
    @Override
    public void doCallbackBiz(Map<String, String> rstMap) {
        pool.submit(() -> {
            String orderNo = rstMap.get("cusorderid");
            String rstStatus = rstMap.get("trxstatus");
            ResultStatusEnum e = ResultStatusEnum.getResultCodeEnum(rstStatus);
            Assert.notNull(e, "非法交易结果,结果编码：{},回调信息:{}", rstStatus, rstMap);
            orderService.orderConfirm(orderNo,e.getPayStatusEnum(),rstMap);
        });
    }

    @Override
    public Map<String, String> queryCharge(Map<String, String> chargeInfoMap) {

        if (null == chargeInfoMap.get("reqsn") && null == chargeInfoMap.get("trxid")) {
            throw new RoadPricingException("reqsn 和trxid 至少有一个不能为空");
        }
        TreeMap<String, String> queryMap = new TreeMap<>();
        queryMap.put("cusid", unionProperties.getCusid());
        queryMap.put("appid", unionProperties.getAppid());
        queryMap.put("version", unionProperties.getVersion());
        if (null != chargeInfoMap.get("reqsn")) {
            queryMap.put("reqsn", chargeInfoMap.get("reqsn"));
        }
        if (null != chargeInfoMap.get("trxid")) {
            queryMap.put("trxid", chargeInfoMap.get("trxid"));
        }
        queryMap.put("randomstr", HashKit.getRandStr(8));
        String sign = SignUtil.sign(queryMap, unionProperties.getKey());
        queryMap.put("sign", sign);
        Map<String, String> rstMap = postreq(unionProperties.getDomain() + "query",queryMap,"查询");
        log.info("交易查询获取结果:{}", rstMap);
        return rstMap;
    }

    @Override
    public Result queryCharge(String orderNo) {
        Map<String,String> map = new HashMap<>(1);
        map.put("reqsn",orderNo);
        try {
            Map<String,String> rstMap = queryCharge(map);
            ResultStatusEnum rse = ResultStatusEnum.getResultCodeEnum(rstMap.get("trxstatus"));
            return new Result(rse.getPayStatusEnum(),rstMap);
        } catch (RoadPricingException e) {
            log.info("查询交易异常",e);
           return new Result(PayStatusEnum.PAY_EXCEPTION,e.getMessage());
        }
    }


    /**
     * 请求支付http
     *
     * @param
     * @return
     */
    private Map<String,String> postreq(String url, Map<String, String> reqMap, String bizTitle) {
        log.info("{}支付请求参数:{}", bizTitle, reqMap);
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(reqMap);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMap, null);
        //  执行HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        //  输出结果
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            throw new RoadPricingException("http/https 请求失败,{}"+ response);
        }

        TreeMap<String,String> map =JSON.parseObject(response.getBody(),TreeMap.class);
        if ("SUCCESS".equals(map.get("retcode"))) {
            //验签
            boolean flag = SignUtil.validSign(map, unionProperties.getKey());
            if (flag) {
                log.info("请求获取通联支付{}交易对象成功:{}", bizTitle, map);
                return map;
            } else {
                throw new RoadPricingException("获取结果签名失败,非法响应结果:{}",JSON.toJSONString(response.getBody()));
            }

        }
        throw new RoadPricingException("FAIL请求响应:{}",JSON.toJSONString(response.getBody()));
    }


}
