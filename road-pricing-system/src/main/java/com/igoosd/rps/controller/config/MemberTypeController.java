package com.igoosd.rps.controller.config;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.MemberType;
import com.igoosd.model.TMemberType;
import com.igoosd.rps.service.MemberTypeService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 2018/5/9.
 */

@RestController
@RequestMapping("/config/memberType")
@Api(tags = "配置管理-会员类型")
public class MemberTypeController {


    @Autowired
    private MemberTypeService memberTypeService;

    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest) {
        Page<TMemberType> page = memberTypeService.findPage(new TMemberType(), new Page(pageRequest.getPageNumber(), pageRequest.getPageSize(), "create_time", false));
        return ResultMsg.resultSuccess(page);
    }

    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "validTime", value = "有效时间(单位：月)", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", required = true, dataType = "string", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(MemberType memberType) {
        Assert.hasText(memberType.getName(), "名称不能为空");
        Assert.notNull(memberType.getValidTime(), "有效时间不能为空");
        Assert.isTrue(memberType.getValidTime() > 0, "有效时间必须大于0");
        //name 校验
        TMemberType temp = new TMemberType();
        temp.setName(memberType.getName());
        int count = memberTypeService.getCount(temp);
        Assert.isTrue(count == 0, "名称重复");

        memberType.setCreateTime(new Date());
        memberTypeService.insert(memberType);

        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "修改", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "long", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "validTime", value = "有效时间(单位：月)", dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "form"),
    })
    @PostMapping("/update")
    public ResultMsg update(MemberType memberType) {
        Assert.notNull(memberType.getId(), "ID不能为空");

        if (memberType.getValidTime() != null) {
            Assert.isTrue(memberType.getValidTime() > 0, "有效时间必须大于0");
        }
        if (!StringUtils.isEmpty(memberType.getName())) {
            //name 校验
            TMemberType temp = new TMemberType();
            temp.setName(memberType.getName());
            List<TMemberType> list = memberTypeService.findList(temp);
            if (!CollectionUtils.isEmpty(list)) {
                Assert.isTrue(list.size() == 1, "名称重复");
                Assert.isTrue(list.get(0).getId().equals(memberType.getId()), "名称重复");
            }
        }

        memberTypeService.update(memberType);

        return ResultMsg.resultSuccess();
    }


    @ApiOperation(value = "查找全部（下拉框接口）", notes = "")
    @ApiImplicitParams({
    })
    @PostMapping("/findAll")
    public ResultMsg findAll(){
        List<TMemberType> list = memberTypeService.findList(new MemberType());
        return ResultMsg.resultSuccess(list);
    }


}
