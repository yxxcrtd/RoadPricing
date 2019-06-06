package com.igoosd.rpss.configure;

import com.igoosd.rpss.configure.interceptor.InterceptorPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 2018/2/6.
 */
@Slf4j
@Configuration
public class WebMvcConfigure extends WebMvcConfigurerAdapter {

    @Autowired
    private List<HandlerInterceptor> interceptors;

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*/

    /**
     * ").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
     * }
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (HandlerInterceptor h : interceptors) {
            InterceptorPath path = (InterceptorPath) h;
            registry.addInterceptor(h).addPathPatterns(path.getInterceptPaths()).excludePathPatterns(path.getExcludePaths());
        }
    }
}
