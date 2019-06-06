package com.igoosd.rpss.pay.confirm;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PaymentStatusEnum;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsOrderConfirmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/5.
 */
@Slf4j
@Service
@Order(3)
public class ArrearsChargeOrderConfirmServiceImpl extends AbsOrderConfirmService {

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;

    @Override
    public int getOrderType() {
        return OrderTypeEnum.ARREARS_CHARGE_ORDER.getValue();
    }

    @Override
    protected void confirmBiz(boolean isSuccess, TOrderItem... items) {
        if (isSuccess) {
            List<Long> ids = new ArrayList<>(items.length);
            for (TOrderItem item :items){
                ids.add(item.getBizId());
            }
            //更新欠费记录状态
            TArrearsRecord tar = new TArrearsRecord();
            tar.setUpdateTime(new Date());
            tar.setPaymentStatus(PaymentStatusEnum.PAID.getValue());
            arrearsRecordMapper.update(tar,new EntityWrapper<TArrearsRecord>().in("id",ids));
            //更新关联停车记录状态
            List<TArrearsRecord> list = arrearsRecordMapper.selectBatchIds(ids);
            for (TArrearsRecord record : list){
                //更新欠费记录关联的停车记录业务状态
                VehicleEntranceRecord tmp = new VehicleEntranceRecord();
                tmp.setBizStatus(BizStatusEnum.ARREARS_CHARGE.getValue());
                tmp.setUpdateTime(new Date());
                tmp.setId(record.getVehicleEntranceId());
                vehicleEntranceRecordMapper.updateById(tmp);
            }
        }
    }

}
