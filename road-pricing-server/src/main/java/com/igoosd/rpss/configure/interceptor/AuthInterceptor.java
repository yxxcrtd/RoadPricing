package com.igoosd.rpss.configure.interceptor;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.common.util.Const;
import com.igoosd.common.util.HashKit;
import com.igoosd.rpss.util.ParamUtils;
import com.igoosd.rpss.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 2018/4/11.
 */
@Component
@Slf4j
@Order(2)
public class AuthInterceptor implements HandlerInterceptor, InterceptorPath {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private boolean flag = true; // 生产开启

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String sessionId = request.getHeader(Const.HEADER_SESSION_ID);
        String signature = request.getHeader(Const.HEADER_SIGNATURE);

        response.setHeader("Content-Type", "application/json;charset=utf8");
        response.setCharacterEncoding("UTF-8");

        if (StringUtils.isEmpty(sessionId)) {
            ResultMsg resultMsg = ResultMsg.resultFail(410, "Header SessionId 参数不匹配");
            response.getWriter().write(JSON.toJSONString(resultMsg));
            response.getWriter().flush();
            return false;
        }
        String redisKey = Const.REDIS_TOKEN_PRE_KEY + sessionId;
        Map<Object, Object> tokenMap = redisTemplate.opsForHash().entries(redisKey);
        String token = (String) tokenMap.get(Const.SESSION_TOKEN);
        String userJsonStr = (String) tokenMap.get(Const.SESSION_USER);
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty(userJsonStr)) {
            log.error("用户信息缓存数据不存在...");
            ResultMsg resultMsg = ResultMsg.resultFail(411, "无效的session");
            response.getWriter().write(JSON.toJSONString(resultMsg));
            response.getWriter().flush();
            return false;
        }
        if (flag) {
            if (StringUtils.isEmpty(signature)) {
                ResultMsg resultMsg = ResultMsg.resultFail(410, "Header signature 参数不匹配");
                response.getWriter().write(JSON.toJSONString(resultMsg));
                response.getWriter().flush();
                return false;
            }

            if (StringUtils.isEmpty(token)) {
                log.error("用户信息缓存数据不存在...");
                ResultMsg resultMsg = ResultMsg.resultFail(412, "无效的token");
                response.getWriter().write(JSON.toJSONString(resultMsg));
                response.getWriter().flush();
                return false;
            }
            //时间错校验
            String timestamp = request.getParameter("timestamp");
            if (StringUtils.isEmpty(timestamp)) {
                log.error("指定的timestamp不存在...");
                ResultMsg resultMsg = ResultMsg.resultFail(411, "无效的参数或参数已过期");
                response.getWriter().write(JSON.toJSONString(resultMsg));
                response.getWriter().flush();
                return false;
            }else{
                //时间错校验 // 可选 校验 是否在允许范围内 TODO
            }
            String sortParamStr = ParamUtils.sortRequestFormParams(request);

            log.info("请求参数排序后：{}", sortParamStr);
            String curSignature = HashKit.md5(token + sortParamStr);
            //签名校验
            if (!curSignature.equals(signature)) {
                ResultMsg resultMsg = ResultMsg.resultFail(414, "签名失败");
                response.getWriter().write(JSON.toJSONString(resultMsg));
                response.getWriter().flush();
                return false;
            }
        }
        LoginUser.set(JSON.parseObject(userJsonStr, LoginUser.class));
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
        return new String[]{
                "/api/arrears/queryList",//欠费记录列表
                "/api/arrears/detailInfo",//欠费详情
                "/pay/callback",//支付回调
                "/wechat/**"//微信
        };
    }

    @Override
    public String[] getInterceptPaths() {
        return new String[]{
                "/api/**",
                "/pay/**",
                "/common/logout"
        };
    }
}
