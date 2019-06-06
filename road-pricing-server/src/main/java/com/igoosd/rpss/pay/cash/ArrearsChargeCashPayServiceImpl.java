package com.igoosd.rpss.pay.cash;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PaymentStatusEnum;
import com.igoosd.domain.VehicleEntranceRecord;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsCashPayService;
import com.igoosd.rpss.vo.BusinessInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/1.
 */
@Service
public class ArrearsChargeCashPayServiceImpl extends AbsCashPayService {

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;

    @Override
    public OrderTypeEnum getOrderTypeEnum() {
        return OrderTypeEnum.ARREARS_CHARGE_ORDER;
    }


    @Override
    protected List<TOrderItem> getOrderItemsForBiz(BusinessInfo businessInfo) {
        List<TArrearsRecord> list = arrearsRecordMapper.selectList(new EntityWrapper<TArrearsRecord>().in("id", businessInfo.getArrearsRecordIds()).eq("payment_status", PaymentStatusEnum.UNPAID.getValue()));
        Assert.isTrue(businessInfo.getArrearsRecordIds().size() == list.size(),"所选欠费记录总数和未支付欠费记录条数不符");
        //更新 校验总数是否符合
        arrearsRecordMapper.confirmOrderForArrearsRecords(PaymentStatusEnum.PAID.getValue(),businessInfo.getArrearsRecordIds());
        //更新停车记录
        for (TArrearsRecord record : list){
            //更新欠费记录关联的停车记录业务状态
            VehicleEntranceRecord tmp = new VehicleEntranceRecord();
            tmp.setBizStatus(BizStatusEnum.ARREARS_CHARGE.getValue());
            tmp.setUpdateTime(new Date());
            tmp.setId(record.getVehicleEntranceId());
            vehicleEntranceRecordMapper.updateById(tmp);
        }
        List<TOrderItem> items = new ArrayList<>(list.size());
        for (TArrearsRecord record : list) {
            TOrderItem item = new TOrderItem();
            item.setBizType(OrderBizTypeEnum.ARREARS_RECHARGE.getValue());
            item.setBizId(record.getId());
            item.setPayAmount(record.getArrearsAmount());
            items.add(item);
        }
        return items;
    }

    @Override
    protected void postDoBiz(BusinessInfo businessInfo) {
        //无需处理
    }

}
