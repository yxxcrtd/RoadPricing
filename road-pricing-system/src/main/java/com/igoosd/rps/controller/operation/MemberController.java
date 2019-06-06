package com.igoosd.rps.controller.operation;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.DateUtil;
import com.igoosd.common.util.RegexUtils;
import com.igoosd.domain.Member;
import com.igoosd.model.TMember;
import com.igoosd.model.TMemberType;
import com.igoosd.model.TSubParking;
import com.igoosd.rps.service.MemberService;
import com.igoosd.rps.service.MemberTypeService;
import com.igoosd.rps.service.SubParkingService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * 2018/5/10.
 */
@Api(tags = "运营管理-会员管理")
@RestController
@RequestMapping("/operation/member")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private SubParkingService subParkingService;
    @Autowired
    private MemberTypeService memberTypeService;


    @ApiOperation(value = "分页查询", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "路段ID",  dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "subParkingName", value = "路段名",  dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "carNumber", value = "车牌号",  dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "memberTypeId", value = "会员类型",  dataType = "int", paramType = "form"),

    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, Member member) {
        Page<Member> page = memberService.fuzzyFindPage(member, new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize(), "createTime", false));
        return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "carNumber", value = "车牌号", required = true,  dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "subParkingId", value = "所属路段 0表示全部", required = true,  dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "memberTypeId", value = "会员类型",required = true,   dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "chargeAmount", value = "交易金额",  required = true,dataType = "decimal", paramType = "form"),
    })
    @PostMapping("/insert")
    public ResultMsg insert(Member member){
        Assert.hasText(member.getCarNumber(),"车牌号不能为空");
        Assert.notNull(member.getSubParkingId(),"路段不能为空");
        Assert.notNull(member.getMemberTypeId(),"会员类型不能为空");
        Assert.notNull(member.getStartDate(),"收费开始时间不能为空");
        Assert.notNull(member.getChargeAmount(),"交易金额不能为空");
        //车牌号校验
        Assert.isTrue(RegexUtils.isCarNumber(member.getCarNumber()),"不合法的车牌号");
        //路段校验
        if(0 != member.getSubParkingId()){
            TSubParking subParking = subParkingService.getEntityByKey(member.getSubParkingId());
          Assert.notNull(subParking,"不合法的路段");
        }
        //会员类型校验
        TMemberType memberType = memberTypeService.getEntityByKey(member.getMemberTypeId());
        Assert.notNull(memberType,"不合法的会员类型");
        //开始时间校验
        Assert.isTrue(member.getStartDate().compareTo(DateUtil.convertYmdDate(new Date()))>=0,"开始日期不能小于当日日期");
        Member latestMember = memberService.getLatestMember(member.getCarNumber());
        if(latestMember != null){
            Assert.isTrue(member.getStartDate().compareTo(latestMember.getEndDate())>0,"当前存在生效的会员截止日期:"+ DateUtil.convertYmdDate(latestMember.getEndDate())+"大于新建会员的开始日期");
        }
        //开始创建会员
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(member.getStartDate());
        calendar.add(Calendar.MONTH,memberType.getValidTime());
        member.setEndDate(calendar.getTime());
        member.setCreateTime(new Date());
        member.setCreator("admin");//暂时写死 TODO

        memberService.insert(member);
        return ResultMsg.resultSuccess("新增会员成功");
    }


    @ApiOperation(value = "新增", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true,  dataType = "string", paramType = "form"),
    })
    @PostMapping("/delete")
    public ResultMsg delete(Long id){
        //只能删除即将生效的
        TMember member = memberService.getEntityByKey(id);
        Assert.notNull(member,"无效的ID");
        Assert.isTrue(member.getStartDate().compareTo(DateUtil.convertYmdDate(new Date()))>=0,"会员已生效的记录无法删除");
        memberService.deleteByKey(id);
        return ResultMsg.resultSuccess();
    }

}
