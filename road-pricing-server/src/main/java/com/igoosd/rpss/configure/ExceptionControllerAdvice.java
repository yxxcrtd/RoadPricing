package com.igoosd.rpss.configure;

import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.model.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 2018/3/28.
 * 异常控制层增强 统一异常处理框架
 */
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(RoadPricingException.class)
    @ResponseBody
    public ResultMsg handleException(Exception e) {
        if(!RoadPricingException.class.isAssignableFrom(e.getClass())){
            log.error("发生系统异常",e);
        }else {
            log.info("发生业务异常:{}",e.getMessage());
        }
        return ResultMsg.resultFail(-200, e.getMessage());
    }

}
