package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.ChargeRuleTypeEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.Const;
import com.igoosd.domain.ChargeRule;
import com.igoosd.model.TChargeRule;
import com.igoosd.rps.service.ChargeRuleService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 2018/5/9.
 */

@RestController
@RequestMapping("/config/chargeRule")
@Api(tags = "配置管理-收费规则")
public class ChargeRuleController {

    @Autowired
    private ChargeRuleService chargeRuleService;
    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest){
       Page<TChargeRule> page =  chargeRuleService.findPage(new ChargeRule(),new Page(pageRequest.getPageNumber(),pageRequest.getPageSize(),"create_time",false));
       return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始收费时间(xx:xx 格式)", required = true, dataType = "dateTime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "截止收费时间(xx:xx 格式)", required = true, dataType = "dateTime", paramType = "form"),
            @ApiImplicitParam(name = "ruleContent", value = "规则内容", required = true, dataType = "time", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "time", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg  insert(ChargeRule chargeRule){



        Assert.hasText(chargeRule.getName(),"收费规则名称不能为空");
        Assert.hasText(chargeRule.getRuleContent(),"规则内容不能为空");
        Assert.notNull(chargeRule.getStartTime(),"收费开始时间不能为空");
        Assert.notNull(chargeRule.getEndTime(),"收费截止时间不能为空");

        //删除缓存收费规则
        redisTemplate.delete(Const.REDIS_CHARGE_RULE_HASH_KEY);
        //写死规则类型为分时段收费类型
        chargeRule.setRuleType(ChargeRuleTypeEnum.TIME_AREA.getValue());
        chargeRule.setCreateTime(new Date());
        chargeRuleService.insert(chargeRule);
        return ResultMsg.resultSuccess("收费规则创建成果");

    }


    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "开始收费时间(xx:xx 格式)", required = true, dataType = "time", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "截止收费时间(xx:xx 格式)", required = true, dataType = "time", paramType = "form"),
            @ApiImplicitParam(name = "ruleContent", value = "规则内容", required = true, dataType = "time", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "time", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(ChargeRule chargeRule){
        Assert.notNull(chargeRule.getId(),"ID不能为空");

        //删除缓存收费规则
        redisTemplate.delete(Const.REDIS_CHARGE_RULE_HASH_KEY);

        chargeRuleService.update(chargeRule);
        return ResultMsg.resultSuccess("收费规则修改成功");
    }

    @ApiOperation(value = "查找所有收费规则(其他页面下拉框使用)", notes = "")
    @ApiImplicitParams({
    })
    @PostMapping("findAll")
    public ResultMsg findAll(){
        List<TChargeRule> list = chargeRuleService.findList(new ChargeRule());
        return ResultMsg.resultSuccess(list);
    }
}
