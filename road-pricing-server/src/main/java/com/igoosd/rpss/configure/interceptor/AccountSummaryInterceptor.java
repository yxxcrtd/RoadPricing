package com.igoosd.rpss.configure.interceptor;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.DateUtil;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.rpss.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 2018/4/11.
 */
@Component
@Order(3)
public class AccountSummaryInterceptor implements InterceptorPath,HandlerInterceptor {

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        LoginUser loginUser = LoginUser.get();
        //扎帐校验
        TAccountSummary temp = new TAccountSummary();
        temp.setCollectorId(loginUser.getCollectorId());
        temp.setSummaryDate(DateUtil.convertYmdDate(new Date()));
        TAccountSummary tas = accountSummaryMapper.selectOne(temp);
        if (null != tas) {
            if ((tas.getSummaryStatus() & SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue())
                    == SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue()) {
                ResultMsg resultMsg = ResultMsg.resultFail(-200, "当前用户已扎帐，本次操作拒绝");
                response.getWriter().write(JSON.toJSONString(resultMsg));
                response.getWriter().flush();
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public String[] getExcludePaths() {
        return  new String[]{
                "/api/arrears/**",//欠费相关接口
                "/api/main/**",//首页相关接口过滤
                "/api/collector/totalChargeInfo",
                "/api/print/**",//打印接口放开
                "/pay/callback",//支付回调
                "/wechat/**",//微信
                "/api/pay/confirm"//支付回调查询接口
        };
    }

    @Override
    public String[] getInterceptPaths() {
        return new String[]{
                "/api/**",
                "/pay/**"
        };
    }
}
