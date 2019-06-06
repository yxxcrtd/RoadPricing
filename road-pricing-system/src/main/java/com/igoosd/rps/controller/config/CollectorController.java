package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.JobStatusEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.HashKit;
import com.igoosd.domain.Collector;
import com.igoosd.model.TCollector;
import com.igoosd.rps.service.CollectorService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 2018/5/9.
 */
@RestController
@RequestMapping("/config/collector")
@Api(tags = "配置管理-收费员管理")
public class CollectorController {


    private static final String INIT_PWD = "00000000";


    @Autowired
    private CollectorService collectorService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobNumber", value = "工号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobStatus", value = "状态（0：离职 1在职）", dataType = "int", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, Collector collector) {

        Page<Collector> page = collectorService.fuzzyFindPage(collector, new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), "create_time", false));
        return ResultMsg.resultSuccess(page);
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobNumber", value = "工号", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobStatus", value = "状态（0：离职 1在职）", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sex", value = "性别(true:男 false:女,)", dataType = "boolean", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "家庭住址", dataType = "string", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(Collector collector) {
        Assert.hasText(collector.getName(), "名称不能为空");
        Assert.hasText(collector.getJobNumber(), "工号不能为空");
        Assert.notNull(collector.getJobStatus(), "员工状态不能为空");
        Assert.isTrue((JobStatusEnum.On_JOB.getValue() == collector.getJobStatus() ||
                JobStatusEnum.QUIT_JOB.getValue() == collector.getJobStatus()), "员工状态不合法");
        //工号校验
        Collector temp = new Collector();
        temp.setJobNumber(collector.getJobNumber());
        int count = collectorService.getCount(temp);
        Assert.isTrue(count == 0, "工号重复");

        collector.setPassword(HashKit.md5(INIT_PWD));
        collector.setCreateTime(new Date());
        collectorService.insert(collector);

        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobNumber", value = "工号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "jobStatus", value = "状态（0：离职 1在职）", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "sex", value = "性别(true:男 false:女,)", dataType = "boolean", paramType = "form"),
            @ApiImplicitParam(name = "address", value = "家庭住址", dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(Collector collector) {

        Assert.notNull(collector.getId(), "主键不能为空");
        if (collector.getJobStatus() != null) {
            Assert.notNull((JobStatusEnum.On_JOB.getValue() == collector.getJobStatus() ||
                    JobStatusEnum.QUIT_JOB.getValue() == collector.getJobStatus()), "员工状态不合法");
        }

        TCollector oldCollector = collectorService.getEntityByKey(collector.getId());
        Assert.notNull(oldCollector,"找不到指定的收费员信息");

        if(!StringUtils.isEmpty(collector.getJobNumber())){
            //工号校验
            Collector temp = new Collector();
            temp.setJobNumber(collector.getJobNumber());
            List<TCollector> list = collectorService.findList(temp);
            if(!CollectionUtils.isEmpty(list)){
                Assert.isTrue(list.size()==1,"工号重复");
                Assert.isTrue(list.get(0).getId().equals(collector.getId()),"工号重复");
            }
        }
        collector.setPassword(null);
        collectorService.update(collector);

        return ResultMsg.resultSuccess();
    }

    @ApiOperation(value = "初始化密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/initPwd")
    public ResultMsg initPwd(Long id){

        TCollector oldCollector = collectorService.getEntityByKey(id);
        Assert.notNull(oldCollector,"找不到指定的收费员信息");
        
        TCollector temp = new TCollector();
        temp.setId(id);
        temp.setPassword(HashKit.md5(INIT_PWD));

        collectorService.update(temp);
        return ResultMsg.resultSuccess("初始化密码成功，密码："+INIT_PWD);
    }

    @ApiOperation(value = "离职/在职", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/changeStatus")
    public ResultMsg changeStatus(Long id){
        TCollector oldCollector = collectorService.getEntityByKey(id);
        Assert.notNull(oldCollector,"找不到指定的收费员信息");

        TCollector temp = new TCollector();
        temp.setId(id);
        temp.setJobStatus((oldCollector.getJobStatus()+1)%2);
        collectorService.update(temp);
        return ResultMsg.resultSuccess();
    }

    @ApiOperation(value = "查找全部", notes = "")
    @ApiImplicitParams({
    })
    @PostMapping("/findAll")
    public ResultMsg findAll(){
       List<TCollector> list =  collectorService.findList(new TCollector());
       return ResultMsg.resultSuccess(list);
    }
}
