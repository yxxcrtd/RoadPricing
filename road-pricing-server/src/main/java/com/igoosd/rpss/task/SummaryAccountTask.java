package com.igoosd.rpss.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.enums.SummaryStatusEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.AccountBalance;
import com.igoosd.domain.Order;
import com.igoosd.mapper.AccountBalanceMapper;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.DutyRecordMapper;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.model.TOrder;
import com.igoosd.rpss.pay.PayService;
import com.igoosd.rpss.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/16.
 * 系统 为 未扎帐用户清算订单资金
 * 扎 线上账
 * 存在一笔可以账目都不允许扎帐成功
 */
@Service
@Slf4j
@org.springframework.core.annotation.Order(4)
public class SummaryAccountTask extends AbsTask {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PayService payService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DutyRecordMapper dutyRecordMapper;
    @Autowired
    private AccountSummaryMapper accountSummaryMapper;

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private AccountBalanceMapper accountBalanceMapper;

    @Scheduled(cron = "0 0 23 * * ?")
    public void doTask() {
        Date date = new Date();
        doTask(date);
    }

    public void doTask(Date date) {
        if (!preTask()) {
            return;
        }
        log.info("----------定时调度扎帐统计任务开始--------------");
        //当日存在 超时、支付中、处理中 订单进行分析和确认 确保当日所有订单都为完结状态（成功、失败、取消）
        Object[] psArray = new Object[3];
        psArray[0] = PayStatusEnum.PRE_PAYING.getValue();
        psArray[1] = PayStatusEnum.PAY_HANDING.getValue();
        //处理中间状态订单
        Date startDate = DateUtil.convertYmdDate(date);
        Date endDate = DateUtil.getNextDate(startDate);
        List<TOrder> list = orderMapper.selectList(new EntityWrapper<TOrder>().in("pay_status", psArray)
                .between("create_time", startDate, endDate));
        for (TOrder order : list) {
            //调用查询交易接口确认订单是否完成
            String orderNo = order.getOrderNo();
            PayService.Result result;
            try {
                result = payService.queryCharge(orderNo);
            } catch (RoadPricingException e) {
                result = new PayService.Result(PayStatusEnum.PAY_EXCEPTION, e.getMessage());
            }
            orderService.orderConfirm(order, result.getPayStatusEnum(), result.getQueryResult());
        }
        int count = orderMapper.selectCount(new EntityWrapper<TOrder>().in("pay_status", psArray)
                .between("create_time", startDate, endDate));
        Assert.isTrue(count == 0, "日期：{}定时调度查询交易，存在待支付或者处理中状态订单，请管理员二次确认", DateUtil.formatYmdDate(startDate));
        //扎帐--线上 线下（未扎帐）--以登录日志员工作为记住扎帐汇总
        List<Long> collectorIds = dutyRecordMapper.getCurDateDutyCollectorIds();
        if (!CollectionUtils.isEmpty(collectorIds)) {
            //线下已扎帐记录
            List<TAccountSummary> summaryedList = accountSummaryMapper.selectList(
                    new EntityWrapper<TAccountSummary>().in("collector_id", collectorIds)
                            .between("summary_date", startDate, endDate));
            for (Long collectorId : collectorIds) {
                //获取当日总收入
                Order order = new Order();
                order.setStartTime(DateUtil.convertYmdDate(new Date()));
                order.setEndTime(DateUtil.getNextDate(order.getStartTime()));
                order.setPayStatus(PayStatusEnum.PAY_SUCCESS.getValue());
                order.setCollectorId(collectorId);
                BigDecimal totalAmount = orderService.getOrderAmount(order);
                totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
                //线下
                order.setPayWay(PayWayEnum.CASH_PAY.getValue());
                BigDecimal offlineAmount = orderService.getOrderAmount(order);
                offlineAmount = offlineAmount != null ? orderService.getOrderAmount(order) : BigDecimal.ZERO;
                //线上
                BigDecimal onlineAmount = totalAmount.subtract(offlineAmount);
                //查询当日收费员是否存在扎帐记录
                boolean isExist =  false;
                //扎帐处理
                for (int i = 0; i < summaryedList.size(); i++) {
                    TAccountSummary tas = summaryedList.get(i);
                    if (tas.getCollectorId().equals(collectorId)) {
                        TAccountSummary temp = new TAccountSummary();
                        temp.setId(tas.getId());
                        temp.setOnlineAmount(onlineAmount);
                        temp.setTotalAmount(totalAmount);
                        temp.setSummaryStatus(SummaryStatusEnum.ONLINE_SUMMARY_SUCCESS.getValue() | tas.getSummaryStatus());
                        if(!tas.getSummaryStatus().equals(SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue())){
                            temp.setOfflineAmount(offlineAmount);
                            if(offlineAmount.compareTo(BigDecimal.ZERO) == 0){
                                temp.setSummaryStatus(SummaryStatusEnum.OFF_SUMMARY_SUCCESS.getValue() | tas.getSummaryStatus());
                            }
                        }else{
                            log.info("收费员:{}未扎帐，系统开始线上扎帐",collectorId);
                        }
                        accountSummaryMapper.updateById(temp);
                        summaryedList.remove(i);
                        isExist = true;
                        break;
                    }
                }
                if(isExist){
                    continue;
                }
                log.info("收费员:{}未扎帐，系统开始线上扎帐");
                TAccountSummary temp = new TAccountSummary();
                temp.setCollectorId(collectorId);
                temp.setSummaryStatus(offlineAmount.compareTo(BigDecimal.ZERO) > 0 ?
                        SummaryStatusEnum.ONLINE_SUMMARY_SUCCESS.getValue() : SummaryStatusEnum.SUMMARY_SUCCESS.getValue());
                temp.setSummaryDate(startDate);
                temp.setCreateTime(new Date());
                temp.setOfflineAmount(offlineAmount);
                temp.setOnlineAmount(onlineAmount);
                temp.setTotalAmount(totalAmount);
                accountSummaryMapper.insert(temp);
            }
        }
        //每日统计任务记录
        //收费员统计报表--扎帐
        //每日对账报表统计
        BigDecimal totalArrearsChargeAmount = arrearsRecordMapper.getSumArrearsChargeAmount(startDate, endDate);
        BigDecimal totalParkingChargeAmount = vehicleEntranceRecordMapper.getSumParkingChargeAmount(startDate, endDate);
        BigDecimal totalParkingRealChargeAmount = vehicleEntranceRecordMapper.getSumRealParkingChargeAmount(startDate, endDate);
        List<Integer> onlineList = new ArrayList<>(2);
        onlineList.add(PayWayEnum.ALI_PAY.getValue());
        onlineList.add(PayWayEnum.WECHAT_PAY.getValue());
        BigDecimal totalOnlineOrderAmount = orderMapper.getSumPayWayPayAmount(startDate, endDate,onlineList);
        List<Integer> offLineList = new ArrayList<>(1);
        offLineList.add(PayWayEnum.CASH_PAY.getValue());
        BigDecimal totalOfflineOrderAmount = orderMapper.getSumPayWayPayAmount(startDate, endDate, offLineList);

        AccountBalance ab = new AccountBalance();
        ab.setCreateTime(new Date());
        ab.setBalanceDate(startDate);
        ab.setTotalArrearsChargeAmount(totalArrearsChargeAmount == null ? BigDecimal.ZERO : totalArrearsChargeAmount);
        ab.setTotalChargeAmount(totalParkingChargeAmount == null ? BigDecimal.ZERO : totalParkingChargeAmount);
        ab.setTotalRealChargeAmount(totalParkingRealChargeAmount == null ? BigDecimal.ZERO : totalParkingRealChargeAmount);
        ab.setTotalMemberPreferentialAmount(ab.getTotalChargeAmount().subtract(ab.getTotalRealChargeAmount()));
        ab.setTotalOnlineAmount(totalOnlineOrderAmount == null ? BigDecimal.ZERO : totalOnlineOrderAmount);
        ab.setTotalOfflineAmount(totalOfflineOrderAmount == null ? BigDecimal.ZERO : totalOfflineOrderAmount);

        accountBalanceMapper.insert(ab);
        log.info("{},对账功能完成", startDate);
        log.info("----------定时调度扎帐统计任务完成--------------");
    }

}
