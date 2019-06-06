package com.igoosd.rpss.api;

import com.igoosd.rpss.pay.PayService;
import com.igoosd.rpss.pay.union.util.SignUtil;
import com.igoosd.rpss.pay.union.util.UnionProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2018/3/21.
 */
@Controller
@Slf4j
public class PayCallBackController {

    private static final Logger CALLBACK_LOG = LoggerFactory.getLogger("PAY_CALLBACK_LOGGER");

    @Autowired
    private UnionProperties unionProperties;
    @Autowired
    private PayService payService;

    @PostMapping("/pay/callback")
    public void callBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Map<String,String[]> oldMap = request.getParameterMap();
        if(null == oldMap || oldMap.size() ==0){
            log.info("接收到异常回调消息");
            return;
        }
        TreeMap<String,String> treeMap = new TreeMap<>();
        for (Map.Entry<String,String[]> entry: oldMap.entrySet()){
            treeMap.put(entry.getKey(),entry.getValue()[0]);
        }
        log.info("获取支付回调结果:{}",treeMap);
        //验签
        boolean flag = SignUtil.validSign(treeMap,unionProperties.getKey());
        if(flag){
            CALLBACK_LOG.info("获取验签成功的支付结果数据:{}",treeMap);
            //异步调用处理业务
            payService.doCallbackBiz(treeMap);
            response.getWriter().write("success");
            response.getWriter().flush();
        }else{
            log.info("回调消息签名异常...");
        }
    }
}
