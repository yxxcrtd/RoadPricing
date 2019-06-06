package com.igoosd.rpss.pay.confirm;

import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TOrderItem;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.cache.RedisCacheService;
import com.igoosd.rpss.pay.AbsOrderConfirmService;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.util.RpssConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 2018/3/5.
 */
@Service
@Order(1)
@Slf4j
public class ParkingChargeOrderConfirmServiceImpl extends AbsOrderConfirmService {

    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ConfigService configService;

    @Override
    public int getOrderType() {
        return OrderTypeEnum.PARKING_CHARGE_ORDER.getValue();
    }

    /**
     * 订单失败的时候 不处理
     *
     * @param isSuccess
     * @param items
     */
    @Override
    protected void confirmBiz(boolean isSuccess, TOrderItem... items) {
        //校验停车记录是否为出场收费中
        TVehicleEntranceRecord ver = vehicleEntranceRecordMapper.selectById(items[0].getBizId());
        // Assert.isTrue(ver.getBizStatus().equals(BizStatusEnum.EXIT_PAYING.getValue()), "停车记录不是出场支付状态");
        if (isSuccess) {

            VehicleEntranceRecord record = new VehicleEntranceRecord();
            record.setBizStatus(BizStatusEnum.CAR_EXIT.getValue());
            record.setId(items[0].getBizId());
            int count = vehicleEntranceRecordMapper.updateById(record);
            if (0 == count) {
                log.warn("订单状态已被修改...本次忽略");
                return ;
            }
            redisCacheService.carExit(ver.getId(), ver.getParkingSpaceId(), Boolean.parseBoolean(configService.getValue(RpssConst.cfg_key_car_free_exit_2nd_confirm)));
        }
    }
}
