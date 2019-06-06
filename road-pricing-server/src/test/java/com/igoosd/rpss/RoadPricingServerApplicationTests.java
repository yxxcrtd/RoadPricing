package com.igoosd.rpss;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.util.DateUtil;
import com.igoosd.common.util.HashKit;
import com.igoosd.domain.User;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.mapper.UserMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TOrder;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.pay.union.UnionPayServiceImpl;
import com.igoosd.rpss.pay.union.util.SignUtil;
import com.igoosd.rpss.pay.union.util.UnionProperties;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.task.CarAutoArrearsExitTask;
import com.igoosd.rpss.task.DutyStatisticsTask;
import com.igoosd.rpss.task.LoginDeniedTask;
import com.igoosd.rpss.task.SummaryAccountTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RoadPricingServerApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UnionPayServiceImpl unionPayService;
    @Value("${union.appid}")
    private String appid;
    @Autowired
    private Environment environment;
    @Autowired
    private UnionProperties unionProperties;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SummaryAccountTask summaryAccountTask;
    @Autowired
    private DutyStatisticsTask dutyStatisticsTask;
    @Autowired
    private CarAutoArrearsExitTask carAutoArrearsExitTask;
    @Autowired
    private LoginDeniedTask loginDeniedTask;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private ChargeRuleService chargeRuleService;




    @Test
    public void testInsert(){
        User user = new User();
        user.setUserName("仁");
        userMapper.insert(user);
    }

    @Test
    public void testSelect(){
       //List<User> list = userMapper.selectTestJoin();
       // System.out.println(list);
    }

    @Test
    public void del(){
        carAutoArrearsExitTask.doTask();
    }

    @Test
    public void unionPay(){
    }

    @Test
    public void queryPay(){
        Map<String,String> map = new HashMap<>(2);
        map.put("reqsn","2018032616085144311215415411");//商户订单号
        //map.put("trxid","111821100000543333");//交易单号
        Map<String,String> rstMap = unionPayService.queryCharge(map);
        System.out.println(rstMap);
    }

    @Test
    public void payCancel(){
        List<TOrder> list = orderMapper.selectList(new EntityWrapper<TOrder>().in("pay_status",new Integer[]{2}).ge("create_time", DateUtil.convertYmdDate(new Date())));
        for (TOrder order : list){
            TreeMap<String,String> reqMap = new TreeMap<>();
            reqMap.put("cusid",unionProperties.getCusid());
            reqMap.put("appid",unionProperties.getAppid());
            reqMap.put("version",unionProperties.getVersion());
            reqMap.put("reqsn", HashKit.md5(System.currentTimeMillis()+""));
            reqMap.put("trxamt",order.getPayAmount().multiply(new BigDecimal(100)).intValue()+"");
            reqMap.put("oldreqsn",order.getOrderNo());
            reqMap.put("randomstr",HashKit.getRandStr(8));
            reqMap.put("sign", SignUtil.sign(reqMap,unionProperties.getKey()));

            MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.setAll(reqMap);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(multiValueMap, null);
            //  执行HTTP请求
            ResponseEntity<String> response = restTemplate.exchange("https://vsp.allinpay.com/apiweb/unitorder/cancel", HttpMethod.POST, requestEntity, String.class);
            log.info("订单ID:{}撤销结果:{}",order.getId(),response.getBody());
        }

        TVehicleEntranceRecord tvh = vehicleEntranceRecordMapper.selectById(2L);
        chargeRuleService.getChargeAmount(1L,tvh.getEnterTime(),tvh.getExitTime());
    }


    @Test
    public void TestTask(){
        loginDeniedTask.doTask();
        carAutoArrearsExitTask.doTask();
        dutyStatisticsTask.doTask();
        summaryAccountTask.doTask();
    }

    @Test
    public void testChargeAmount() {
        TVehicleEntranceRecord tvh = vehicleEntranceRecordMapper.selectById(2L);
        chargeRuleService.getChargeAmount(1L,tvh.getEnterTime(),tvh.getExitTime());
    }
}
