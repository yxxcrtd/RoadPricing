package com.igoosd.rpss.task;

import com.igoosd.common.util.Const;
import com.igoosd.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Set;

/**
 * 2018/3/16.
 */
@Service
@Slf4j
@Order(1)
public class LoginDeniedTask extends AbsTask {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 到达指定时间 将未登出用户session 删除，禁止用户访问
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void doTask() {
        if(!preTask()){
            return ;
        }
        Set<String> keys = redisTemplate.keys(Const.REDIS_TOKEN_PRE_KEY + "*");
        Set<String> userKeys = redisTemplate.keys(Const.REDIS_SESSION_USER_PRE_KEY + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
        if (!CollectionUtils.isEmpty(userKeys)) {
            redisTemplate.delete(userKeys);
        }
        log.info("定时任务:关闭所有用户会话", DateUtil.formatDetailDate(new Date()));
    }
}
