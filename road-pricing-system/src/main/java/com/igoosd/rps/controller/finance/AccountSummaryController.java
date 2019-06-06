package com.igoosd.rps.controller.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.domain.AccountSummary;
import com.igoosd.model.TAccountSummary;
import com.igoosd.rps.service.AccountSummaryService;
import com.igoosd.rps.util.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 2018/5/10.
 */
@RestController
@RequestMapping("/finance/summary")
@Api(tags = "财务管理-扎帐管理")
public class AccountSummaryController {


    @Autowired
    private AccountSummaryService accountSummaryService;

    @ApiOperation(value = "分页查询", notes = "0:待扎帐 1：线下已扎帐 2：线上已扎帐 3：扎帐完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "第几页", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "collectorId", value = "收费员", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "summaryStatus", value = "扎帐状态", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "startTime", value = "扎帐日期-开始日期（yyyy-mm-dd）", dataType = "datetime", paramType = "form"),
            @ApiImplicitParam(name = "endTime", value = "扎帐日期-结束日期（yyyy-mm-dd）", dataType = "datetime", paramType = "form"),
    })
    @PostMapping("/page")
    public ResultMsg page(PageRequest pageRequest, AccountSummary accountSummary) {

        Page<AccountSummary> page = accountSummaryService.fuzzyFindPage(accountSummary, new Page<>(pageRequest.getPageNumber(), pageRequest.getPageSize(), "createTime", false));
        return ResultMsg.resultSuccess(page);
    }


    @ApiOperation(value = "人工扎帐", notes = "只有状态为 2线上已扎帐 才显示按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键", required = true, dataType = "int", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string", paramType = "form"),
    })
    @PostMapping("/manualSummary")
    public ResultMsg manualSummary(@RequestParam Long id, String remark) {

        TAccountSummary tas = accountSummaryService.getEntityByKey(id);
        Assert.notNull(tas, "找不到指定的扎帐记录");
        Assert.isTrue(SummaryStatusEnum.ONLINE_SUMMARY_SUCCESS.getValue() == tas.getSummaryStatus(), "只允许线上已扎帐的扎帐记录进行人工扎帐");

        TAccountSummary temp = new TAccountSummary();
        temp.setId(id);
        if (StringUtils.isEmpty(remark)) {
            temp.setRemark("人工扎帐");
        } else {
            temp.setRemark("人工扎帐-" + remark);
        }
        temp.setSummaryTime(new Date());
        temp.setSummaryStatus(SummaryStatusEnum.SUMMARY_SUCCESS.getValue());

        accountSummaryService.update(temp);

        return ResultMsg.resultSuccess("手工扎帐成功");
    }
}
