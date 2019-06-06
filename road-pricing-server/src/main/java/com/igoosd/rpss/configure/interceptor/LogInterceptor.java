package com.igoosd.rpss.configure.interceptor;

import com.igoosd.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 2018/4/10.
 */
@Slf4j
@Component
@Order(1)
public class LogInterceptor implements HandlerInterceptor,InterceptorPath {



        // before the actual handler will be executed
        public boolean preHandle(HttpServletRequest request,
                                 HttpServletResponse response, Object handler) throws Exception {
            long startTime = System.currentTimeMillis();
            request.setAttribute("startTime", startTime);
            if (handler instanceof HandlerMethod) {
                StringBuilder sb = new StringBuilder(1000);

                sb.append("\n-----------------------").append(DateUtil.formatDetailDate(new Date()))
                        .append("-------------------------------------\n");
                HandlerMethod h = (HandlerMethod) handler;
                sb.append("Controller: ").append(h.getBean().getClass().getName()).append("\n");
                sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
                sb.append("Params    : ").append(getParamString(request.getParameterMap())).append("\n");
                sb.append("URI       : ").append(request.getRequestURI()).append("\n");
                log.info(sb.toString());
            }
            return true;
        }

        // after the handler is executed
        public void postHandle(HttpServletRequest request,
                               HttpServletResponse response, Object handler,
                               ModelAndView modelAndView) throws Exception {
            long startTime = (Long) request.getAttribute("startTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            if(handler instanceof HandlerMethod){
                StringBuilder sb = new StringBuilder(1000);
                sb.append("CostTime  : ").append(executeTime).append("ms").append("\n");
                sb.append("-------------------------------------------------------------------------------");
                log.info(sb.toString());
            }
        }

        private String getParamString(Map<String, String[]> map) {
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String,String[]> e:map.entrySet()){
                sb.append(e.getKey()).append("=");
                String[] value = e.getValue();
                if(value != null && value.length == 1){
                    sb.append(value[0]).append("\t");
                }else{
                    sb.append(Arrays.toString(value)).append("\t");
                }
            }
            return sb.toString();
        }

        public void afterCompletion(HttpServletRequest arg0,
                                    HttpServletResponse arg1, Object arg2, Exception arg3)
                throws Exception {

        }

    @Override
    public String[] getExcludePaths() {
        return  new String[]{
               /* "/common/rsa",
                "/common/login",
                "/api/arrears/queryList",//欠费记录列表
                "/api/arrears/detailInfo",//欠费详情
                "/pay/callback",//支付回调
                "/wechat*//**"//微信*/
        };
    }

    @Override
    public String[] getInterceptPaths() {
        return new String[]{
                "/**"
        };
    }
}
