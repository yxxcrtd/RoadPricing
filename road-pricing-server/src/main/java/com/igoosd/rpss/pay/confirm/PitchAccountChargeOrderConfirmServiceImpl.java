package com.igoosd.rpss.pay.confirm;

import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.model.TOrderItem;
import com.igoosd.rpss.pay.AbsOrderConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * 2018/3/5.
 */
@Service
@Order(4)
public class PitchAccountChargeOrderConfirmServiceImpl extends AbsOrderConfirmService {

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;


    @Override
    public int getOrderType() {
        return OrderTypeEnum.PITCH_ACCOUNT_CHARGE_ORDER.getValue();
    }

    @Override
    protected void confirmBiz(boolean isSuccess, TOrderItem... items) {
        if(isSuccess){
            TAccountSummary oldTas = accountSummaryMapper.selectById(items[0].getBizId());
            TAccountSummary tas = new TAccountSummary();
            tas.setId(oldTas.getId());
            tas.setSummaryStatus(oldTas.getSummaryStatus() | SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue());
            accountSummaryMapper.updateById(tas);
        }
    }
}
