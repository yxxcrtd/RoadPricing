package com.igoosd.rpss.task;

import com.igoosd.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 2018/4/8.
 */
public abstract class AbsTask {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    protected boolean preTask(){
        String key = "Task:"+this.getClass().getSimpleName()+":"+ DateUtil.formatYmdDate(new Date());
        boolean flag =  redisTemplate.opsForValue().setIfAbsent(key,"1");
        if(flag){
            redisTemplate.expire(key,1, TimeUnit.DAYS);
        }
        return flag;
    }
}
