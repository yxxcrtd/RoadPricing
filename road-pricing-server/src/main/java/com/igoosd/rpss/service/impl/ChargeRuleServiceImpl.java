package com.igoosd.rpss.service.impl;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.ChargeRuleTypeEnum;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.ChargeRule;
import com.igoosd.domain.Member;
import com.igoosd.mapper.ChargeRuleMapper;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.model.TChargeRule;
import com.igoosd.rpss.charge.ChargeService;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.common.util.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2018/2/26.
 */
@Service
@Slf4j
public class ChargeRuleServiceImpl implements ChargeRuleService {

    @Autowired
    private List<ChargeService> chargeServiceList;
    @Autowired
    private ChargeRuleMapper chargeRuleMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @PostConstruct
    protected  void init(){

        List<ChargeRule> list = chargeRuleMapper.getTotalParkingChargeRuleList();
        Map<String,String> map = new HashMap<>(list.size());
        for (ChargeRule rule : list){
            map.put(""+rule.getParkingId(),JSON.toJSONString(rule));
        }

        redisTemplate.opsForHash().putAll(Const.REDIS_CHARGE_RULE_HASH_KEY,map);
        log.info("收费规则初始化完成");
    }

    @Override
    public BigDecimal getChargeAmount(Long parkingId, Date enterTime, Date exitTime) {

        Assert.isTrue(exitTime.compareTo(enterTime) > 0, "驶出时间必须大于驶入时间");

        TChargeRule chargeRule = getChargeRuleByParkingId(parkingId);

        TreeMap<Object, Object> treeMap = JSON.parseObject(chargeRule.getRuleContent(), TreeMap.class);
        Date startTime = DateUtil.splitDateAndTime(DateUtil.convertYmdDate(enterTime), chargeRule.getStartTime());
        Date endTime = DateUtil.splitDateAndTime(DateUtil.convertYmdDate(exitTime), chargeRule.getEndTime());

        startTime = startTime.compareTo(enterTime) > 0 ? startTime : enterTime;
        endTime = endTime.compareTo(exitTime) < 0 ? endTime : exitTime;
        Long milliSe = endTime.getTime() - startTime.getTime();
        ChargeRuleTypeEnum chargeRuleTypeEnum = ChargeRuleTypeEnum.getChargeRuleTypeEnum(chargeRule.getRuleType());
        for (ChargeService chargeService : chargeServiceList) {
            if (chargeService.getChargeRuleTypeEnum().equals(chargeRuleTypeEnum)) {
                return chargeService.getChargeAmount(milliSe, treeMap);
            }
        }
        log.error("规则配置异常，未找到指定的收费规则类型...");
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getRealChargeAmount(Long parkingId, Long subParkingId, Date enterTime, Date exitTime, String carNumber) {

        Assert.isTrue(exitTime.compareTo(enterTime) > 0, "驶出时间必须大于驶入时间");
        List<Member> list = memberMapper.getMemberListByCarNumber(carNumber, subParkingId, enterTime);
        BigDecimal chargeAmount = BigDecimal.ZERO;
        Date chargeEnterTime = enterTime;
        Date chargeExitTime = exitTime;
        if (!CollectionUtils.isEmpty(list)) {
            for (Member member : list) {
                Date nextEndDate = DateUtil.getNextDate(member.getEndDate());
                if (chargeEnterTime.compareTo(member.getStartDate()) >= 0) {
                    chargeEnterTime = nextEndDate;
                } else {
                    //累加
                    chargeAmount = chargeAmount.add(getChargeAmount(parkingId, chargeEnterTime, member.getStartDate()));
                    chargeEnterTime = nextEndDate;
                }
                if (chargeExitTime.compareTo(chargeEnterTime) <= 0) {
                    return chargeAmount;
                }
            }
        }
        chargeAmount = chargeAmount.add(getChargeAmount(parkingId, chargeEnterTime, chargeExitTime));
        return chargeAmount;
    }

    @Override
    public TChargeRule getChargeRuleByParkingId(Long parkingId) {
        String str = (String)redisTemplate.opsForHash().get(Const.REDIS_CHARGE_RULE_HASH_KEY,""+parkingId);
        if(StringUtils.isEmpty(str)){
            log.warn("找不到指定的收费规则，停车场ID:{}",parkingId);
            this.init();//刷新收费规则
        }
        str = (String)redisTemplate.opsForHash().get(Const.REDIS_CHARGE_RULE_HASH_KEY,""+parkingId);
        ChargeRule chargeRule = JSON.parseObject(str,ChargeRule.class);
        return chargeRule;
    }

}
