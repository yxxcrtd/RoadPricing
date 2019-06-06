package com.igoosd.rpss.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.mapper.ParkingSpaceMapper;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.service.ParkingSpaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 2018/2/5.
 */
@Service
@Slf4j
public class ParkingSpaceServiceImpl implements ParkingSpaceService {


    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    private RedisCacheService redisCacheService;

    public static int limit = 100;

    /**
     * 车位初始化操作
     * 注:
     * 处于性能考虑 在后台人工初始化过程中 忽略检验空闲车位是否存在恶意临时免费出场情况的校验
     * 收费员需主动去全路段范围内去排查确定是否进行入场操作
     */
    @PostConstruct
    public void init() {

        int count = parkingSpaceMapper.selectCount(new EntityWrapper<>());
        log.info("初始化车位信息...共{}个车位", count);
        for (int offset = 0; offset < count; ) {
            List<ParkingSpace> list = parkingSpaceMapper.findParkingSpaceList(offset, limit);
            postInit(list);
            offset += limit;
        }
    }

    protected void postInit(List<ParkingSpace> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (ParkingSpace ps : list) {
                redisCacheService.setParkingSpaceInfo(ps);
            }
        }
    }

    @Override
    public List<Long> findIdsBySubParkingId(Long subParkingId) {
        return parkingSpaceMapper.findIdsBySubParkingId(subParkingId);
    }
}
