package com.igoosd.rps.configure;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.model.ResultMsg;
import com.igoosd.rps.util.SessionUser;
import com.igoosd.rps.util.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 2018/1/17.
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@Slf4j
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    @Autowired
    private WebUtils webUtils;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                                     Object handler) throws Exception {
                SessionUser sessionUser = webUtils.currentUser(request, response);
                if (sessionUser == null) {
                    ResultMsg msg = ResultMsg.resultFail(400, "没有相关权限...");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-type", "application/json;charset=UTF-8");
                    response.getWriter().write(JSON.toJSONString(msg));
                    response.getWriter().flush();
                    return false;
                }
                String path = request.getServletPath();
                log.info("请求路径path:{}", path);
                for (String s : sessionUser.getPermissions()) {
                    if (s.equals(path)) {
                        return true;
                    }
                }
                return false;
            }
        }).addPathPatterns("/main*", "/system*", "/config*", "/duty*", "/operation*", "/finance*", "/logout");
    }

}
