package com.igoosd.rpss.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.util.Const;
import com.igoosd.mapper.SysConfigMapper;
import com.igoosd.model.TSysConfig;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2018/3/27.
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    public String getValue(String key) {
        String value = (String) redisTemplate.opsForHash().get(Const.REDIS_SYS_CONFIG_KEY, key);
        if (StringUtils.isEmpty(value)) {
            List<TSysConfig> list = sysConfigMapper.selectList(new EntityWrapper<>());
            Map<String,String> map = new HashMap<>(list.size());
            for (TSysConfig config : list) {
                map.put(config.getCfgKey(),config.getCfgValue());
            }
            redisTemplate.opsForHash().putAll(Const.REDIS_SYS_CONFIG_KEY,map);
            value = map.get(key);
        }
        return value;
    }




}
