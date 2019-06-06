package com.igoosd.rps.controller.system;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.Const;
import com.igoosd.model.TSysConfig;
import com.igoosd.rps.service.SysConfigService;
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

/**
 * 2018/4/9.
 * 系统参数
 */
@Api(tags = "系统管理-参数管理")
@RestController
@RequestMapping("/system/param")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg findPage(PageRequest pageRequest){
        Page<TSysConfig> page = sysConfigService.findPage(new TSysConfig(),new Page(pageRequest.getPageNumber(),pageRequest.getPageSize(),"id",false));
        return ResultMsg.resultSuccess(page);
    }

    @ApiOperation(value = "修改配置", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置ID", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "cfgValue", value = "配置值", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注",  dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg  update(String remark,String cfgValue,Integer id){
        Assert.hasText(cfgValue,"系统配置值不能为空");
        Assert.notNull(id,"主键不能为空");
        //缓存清除
        redisTemplate.delete(Const.REDIS_SYS_CONFIG_KEY);
        //系统更新
        TSysConfig config = new TSysConfig();
        config.setId(id);
        config.setRemark(remark);
        config.setCfgValue(cfgValue);
        sysConfigService.update(config);
        return ResultMsg.resultSuccess();
    }
}
