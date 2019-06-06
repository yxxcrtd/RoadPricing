package com.igoosd.rpss.pay.pre;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TOrderItem;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.pay.AbsPreOrderService;
import com.igoosd.rpss.service.ChargeRuleService;
import com.igoosd.rpss.vo.BusinessInfo;
import com.igoosd.rpss.vo.CarInfo;
import com.igoosd.rpss.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/1.
 */
@Service
public class ParkingChargePreOrderServiceImpl extends AbsPreOrderService{

    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ChargeRuleService chargeRuleService;


    @Override
    public OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.PARKING_CHARGE_ORDER;
    }

    @Override
    protected List<TOrderItem> getOrderItemsForBiz( BusinessInfo businessInfo) {
        TVehicleEntranceRecord record = vehicleEntranceRecordMapper.selectById(businessInfo.getVehicleEntranceRecordId());
        //停车记录业务状态校验
        Assert.isTrue(record.getBizStatus().equals(BizStatusEnum.CAR_ENTER.getValue())
                || record.getBizStatus().equals(BizStatusEnum.EXIT_PAYING.getValue()),
                "停车记录：{}，当前停车记录状态已完成,停车状态：{}",businessInfo.getVehicleEntranceRecordId(),record.getBizStatus());
        //获取缓存预出场数据若为空以当前服务器时间作为出场时间
        CarInfo carInfo = redisCacheService.getPreExitCarInfo(businessInfo.getVehicleEntranceRecordId());
        VehicleEntranceRecord temp = new VehicleEntranceRecord();
        temp.setId(record.getId());
        temp.setBizStatus(BizStatusEnum.EXIT_PAYING.getValue());
        temp.setUpdateTime(new Date());
        if(carInfo != null){
            temp.setExitTime(carInfo.getPreExitTime());
            temp.setChargeAmount(carInfo.getChargeAmount());
            temp.setRealChargeAmount(carInfo.getRealChargeAmount());
        }else{
            temp.setExitTime(new Date());
            temp.setChargeAmount(chargeRuleService.getChargeAmount(record.getParkingId(),record.getEnterTime(),temp.getExitTime()));
            temp.setRealChargeAmount(chargeRuleService.getRealChargeAmount(record.getParkingId(),record.getSubParkingId(),record.getEnterTime(),temp.getExitTime(),record.getCarNumber()));
        }
        if(LoginUser.get()== null){
            temp.setExitCollectorId(record.getEnterCollectorId());
        }else{
            temp.setExitCollectorId(LoginUser.get().getCollectorId());
        }
        //更新出入场记录
        vehicleEntranceRecordMapper.updateById(temp);

        //创建订单明细列表
        List<TOrderItem> list = new ArrayList<>(1);
        TOrderItem item = new TOrderItem();
        item.setPayAmount(temp.getRealChargeAmount());
        item.setBizType(OrderBizTypeEnum.PARKING_CHARGE.getValue());
        item.setBizId(businessInfo.getVehicleEntranceRecordId());

        list.add(item);
        return list;
    }

}
