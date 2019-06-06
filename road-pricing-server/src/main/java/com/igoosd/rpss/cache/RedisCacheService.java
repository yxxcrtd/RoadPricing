package com.igoosd.rpss.cache;

import com.alibaba.fastjson.JSON;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.ParkingSpaceStatusEnum;
import com.igoosd.domain.ParkingSpace;
import com.igoosd.common.util.Const;
import com.igoosd.rpss.vo.CarInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 2018/2/8.
 */
@Service
public class RedisCacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取入场车位信息
     *
     * @return
     */
    public ParkingSpace getParkingSpaceInfo(Long parkingSpaceId) {
        String json = (String) redisTemplate.opsForHash().get(Const.REDIS_PARKING_SPACE_HASH_KEY, "" + parkingSpaceId);
        ParkingSpace ps = null;
        if (!StringUtils.isEmpty(json)) {
            ps = JSON.parseObject(json, ParkingSpace.class);
        }
        return ps;
    }

    /**
     * 缓存入场车位信息
     *
     * @param ps
     * @return
     */
    public void setParkingSpaceInfo(ParkingSpace ps) {
        redisTemplate.opsForHash().put(Const.REDIS_PARKING_SPACE_HASH_KEY, "" + ps.getId(), JSON.toJSONString(ps));
    }

    /**
     * 获取预出场 车辆及收费等信息
     */
    public CarInfo getPreExitCarInfo(Long verId) {
        String json = redisTemplate.opsForValue().get(Const.REDIS_VER_EXIT_CAR_INFO_PREKEY + verId);
        CarInfo carInfo = null;
        if (!StringUtils.isEmpty(json)) {
            carInfo = JSON.parseObject(json, CarInfo.class);
        }
        return carInfo;
    }

    /**
     * 缓存 预出场 车辆及收费等信息
     * 10分钟缓存时间--后期配置项再行处理
     */
    public void setPreExitCarInfo(CarInfo carInfo, Long verId) {
        String json = JSON.toJSONString(carInfo);
        redisTemplate.opsForValue().set(Const.REDIS_VER_EXIT_CAR_INFO_PREKEY + verId, json, 10L, TimeUnit.MINUTES);
    }


    /**
     * 车辆驶出 缓存清理动作
     *
     * @param verId
     * @param parkingSpaceId
     */
    public void carExit(Long verId, Long parkingSpaceId, boolean freeExitConfirm) {
        RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
        byte[] carInfoKey = redisSerializer.serialize(Const.REDIS_VER_EXIT_CAR_INFO_PREKEY + verId);
        byte[] parkingSpaceKey = redisSerializer.serialize(Const.REDIS_PARKING_SPACE_HASH_KEY);
        byte[] parkingSpaceField = redisSerializer.serialize("" + parkingSpaceId);

        String value = (String) redisTemplate.opsForHash().get(Const.REDIS_PARKING_SPACE_HASH_KEY, "" + parkingSpaceId);
        ParkingSpace ps = JSON.parseObject(value, ParkingSpace.class);
        Assert.notNull(ps, "找不到相关的车位信息");
        redisTemplate.execute((RedisCallback<Object>) redisConnection -> {
            redisConnection.openPipeline();
            redisConnection.del(carInfoKey);
            if (freeExitConfirm) {
                //修改入场车辆状态
                ps.setStatus(ParkingSpaceStatusEnum.FREE_EXIT_CONFIRM.getValue());
            } else {
                ps.setStatus(ParkingSpaceStatusEnum.IDLE.getValue());
                ps.setEnterTime(null);
                ps.setTotalArrearsAmount(null);
                ps.setMemberEndDate(null);
                ps.setMemberStartDate(null);
                ps.setMemberTypeId(null);
                ps.setMemberTypeName(null);
                ps.setCarNumber(null);
                ps.setVehicleEntranceRecordId(null);
            }
            //更新车位信息状态
            byte[] parkingSpaceBytes = redisSerializer.serialize(JSON.toJSONString(ps));
            redisConnection.hSet(parkingSpaceKey, parkingSpaceField, parkingSpaceBytes);
            redisConnection.closePipeline();
            return null;
        });
    }

    public List<ParkingSpace> multiGetParkingSpace(Collection<Long> parkingSpaceIds) {

        List<Object> list = new LinkedList<>();
        Iterator<Long> iterator = parkingSpaceIds.iterator();
        while (iterator.hasNext()) {
            list.add("" + iterator.next());
        }
        List<Object> jsonList = redisTemplate.opsForHash().multiGet(Const.REDIS_PARKING_SPACE_HASH_KEY, list);
        int size = jsonList.size();
        List<ParkingSpace> rstList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String json = (String) jsonList.get(i);
            rstList.add(JSON.parseObject(json, ParkingSpace.class));
        }
        return rstList;
    }




}
