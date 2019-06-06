package com.igoosd.rpss.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.common.enums.OrderBizTypeEnum;
import com.igoosd.common.enums.OrderTypeEnum;
import com.igoosd.common.enums.PayStatusEnum;
import com.igoosd.common.enums.PayWayEnum;
import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.common.util.DateUtil;
import com.igoosd.mapper.AccountSummaryMapper;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.CollectorMapper;
import com.igoosd.mapper.MemberMapper;
import com.igoosd.mapper.MemberTypeMapper;
import com.igoosd.mapper.OrderItemMapper;
import com.igoosd.mapper.OrderMapper;
import com.igoosd.mapper.ParkingSpaceMapper;
import com.igoosd.mapper.SubParkingMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TAccountSummary;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TCollector;
import com.igoosd.model.TMember;
import com.igoosd.model.TMemberType;
import com.igoosd.model.TOrder;
import com.igoosd.model.TOrderItem;
import com.igoosd.model.TParkingSpace;
import com.igoosd.model.TSubParking;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rpss.service.ArrearsService;
import com.igoosd.rpss.service.PrintService;
import com.igoosd.rpss.vo.PrintInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * 2018/4/18.
 */
@Service
public class PrintServiceImpl implements PrintService {


    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;
    @Autowired
    private SubParkingMapper subParkingMapper;
    @Autowired
    private ParkingSpaceMapper parkingSpaceMapper;
    @Autowired
    private CollectorMapper collectorMapper;
    @Autowired
    private ArrearsService arrearsService;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private AccountSummaryMapper accountSummaryMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberTypeMapper memberTypeMapper;


    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public List<PrintInfo> getEnterReceipt(Long verId) {
        TVehicleEntranceRecord tvr = vehicleEntranceRecordMapper.selectById(verId);
        TSubParking subParking = subParkingMapper.selectById(tvr.getSubParkingId());
        TParkingSpace parkingSpace = parkingSpaceMapper.selectById(tvr.getParkingSpaceId());
        TCollector collector = collectorMapper.selectById(tvr.getEnterCollectorId());

        BigDecimal totalArrearsAmount = arrearsService.getTotalArrearsAmount(tvr.getCarNumber());
        totalArrearsAmount = totalArrearsAmount == null ? BigDecimal.ZERO : totalArrearsAmount;
        List<PrintInfo> list = new LinkedList<>();

        list.add(new PrintInfo(1, 1, 1, 1, "车辆驶入凭证"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 17, 0, 1, "车牌号:" + tvr.getCarNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "会员类型 : "+ getMemberTypeNameByMemberId(tvr.getMemberId())));

        list.add(new PrintInfo(0, 0, 0, 0, "停车路段 : " + subParking.getName()));
        list.add(new PrintInfo(0, 0, 0, 0, "泊 位 号 : " + parkingSpace.getCode()));
        list.add(new PrintInfo(0, 0, 0, 0, "员 工 号 : " + collector.getJobNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, "驶入时间 : " + DateUtil.formatStandardDate(tvr.getEnterTime())));
        list.add(new PrintInfo(0, 0, 0, 0, "历史欠费 : " + decimalFormat.format(totalArrearsAmount) + "元"));
        list.add(new PrintInfo(0, 0, 0, 0, "会员类型 : " + getMemberTypeNameByMemberId(tvr.getMemberId())));

        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "免责声明:请关好车窗，妥善保管财物"));
        list.add(new PrintInfo(0, 0, 0, 0, "备注:如未缴费驶出，系统会累计该车辆欠费金额，并依法追缴 监督电话：0550-66666666（安徽谷声科技）12358（物价局）"));
       /* list.add(new PrintInfo(0, 0, 0,0, " "));
        list.add(new PrintInfo(1, 1,"二维码测试内容"));
        list.add(new PrintInfo(0, 0, 0,0, " "));
        list.add(new PrintInfo(1,2, DateUtil.formatDetailDate(new Date())));*/
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "--------------------------------"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));

        return list;
    }

    @Override
    public List<PrintInfo> getExitReceipt(Long verId) {
        TVehicleEntranceRecord tvr = vehicleEntranceRecordMapper.selectById(verId);
        Assert.isTrue(tvr.getBizStatus().equals(BizStatusEnum.CAR_EXIT.getValue()), "非正常缴费出场的停车记录，不允许打印");
        TOrderItem tmp = new TOrderItem();
        tmp.setBizType(OrderBizTypeEnum.PARKING_CHARGE.getValue());
        tmp.setBizId(tvr.getId());
        tmp.setPayStatus(PayStatusEnum.PAY_SUCCESS.getValue());
        TOrderItem item = orderItemMapper.selectOne(tmp);
        TOrder order = null;
        if (null != item) {
            order = orderMapper.selectById(item.getOrderId());
        }
        return getExitPrintInfos(order, tvr);
    }

    @Override
    public List<PrintInfo> printByOrderId(Long orderId) {
        TOrder order = orderMapper.selectById(orderId);
        Assert.notNull(order, "不合法的订单ID");
        Assert.isTrue(PayStatusEnum.PAY_SUCCESS.getValue() == order.getPayStatus(), "非支付成功订单无法进行打印操作");
        switch (OrderTypeEnum.getOrderTypeEnum(order.getOrderType())) {
            case ARREARS_CHARGE_ORDER: {
                return printArrearsByOrder(order);
            }
            case PITCH_ACCOUNT_CHARGE_ORDER: {
                return printSummaryByOrder(order);
            }
            case MERGE_CHARGE_ORDER:
            case PARKING_CHARGE_ORDER: {
                return printExitByOrder(order);
            }
        }
        throw new RoadPricingException("不合法的订单，{}", JSON.toJSONString(order));
    }

    /**
     * 订单打印出场小票
     *
     * @param order
     * @return
     */
    protected List<PrintInfo> printExitByOrder(TOrder order) {

        TOrderItem temp = new TOrderItem();
        temp.setBizType(OrderBizTypeEnum.PARKING_CHARGE.getValue());
        temp.setOrderId(order.getId());
        TOrderItem parkingItem = orderItemMapper.selectOne(temp);
        TVehicleEntranceRecord record = vehicleEntranceRecordMapper.selectById(parkingItem.getBizId());
        return getExitPrintInfos(order, record);
    }

    /**
     * 欠费小票打印
     *
     * @param order
     * @return
     */
    protected List<PrintInfo> printArrearsByOrder(TOrder order) {

        List<TOrderItem> items = orderItemMapper.selectList(new EntityWrapper<TOrderItem>().eq("order_id", order.getId()).last(" limit 1 offset 0"));
        TArrearsRecord record = arrearsRecordMapper.selectById(items.get(0).getBizId());
        int count = orderItemMapper.selectCount(new EntityWrapper<TOrderItem>().eq("order_id", order.getId()));


        List<PrintInfo> list = new LinkedList<>();
        list.add(new PrintInfo(1, 0, 1, 1, "欠费补缴凭证"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "车 牌 号 : " + record.getCarNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, "欠费笔数 : " + count));
        list.add(new PrintInfo(0, 0, 0, 0, "欠费金额 : " + decimalFormat.format(order.getPayAmount()) + " 元"));
        list.add(new PrintInfo(0, 0, 0, 0, "支付方式 : " + PayWayEnum.getPayWayEnum(order.getPayWay()).getName()));
        list.add(new PrintInfo(0, 0, 0, 0, "支付金额 : " + decimalFormat.format(order.getPayAmount()) + " 元"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        //说明
        list.add(new PrintInfo(0, 0, 0, 0, "如您对本次收费存在疑问，欢迎拨打监督电话:0550-66666666(安徽谷声科技有限公司) 12580(物价局)"));
        //分割线
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "---------------------------------"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        return list;
    }

    protected List<PrintInfo> printSummaryByOrder(TOrder order) {
        TOrderItem temp = new TOrderItem();
        temp.setOrderId(order.getId());
        temp.setBizType(OrderBizTypeEnum.SUMMARY_ACCOUNT_CHARGE.getValue());
        TOrderItem item = orderItemMapper.selectOne(temp);
        TAccountSummary tas = accountSummaryMapper.selectById(item.getBizId());
        TCollector collector = collectorMapper.selectById(tas.getCollectorId());

        List<PrintInfo> list = new LinkedList<>();
        list.add(new PrintInfo(1, 1, 16, 1, "扎帐凭证"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "扎帐日期 : " + DateUtil.formatYmdDate(tas.getSummaryDate())));
        list.add(new PrintInfo(0, 0, 0, 0, "扎帐时间 : " + DateUtil.formatStandardDate(tas.getSummaryTime())));
        list.add(new PrintInfo(0, 0, 0, 0, "员工姓名 : " + collector.getName()));
        list.add(new PrintInfo(0, 0, 0, 0, "员 工 号 : " + collector.getJobNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, "支付方式 : " + PayWayEnum.getPayWayEnum(order.getPayWay()).getName()));
        list.add(new PrintInfo(0, 0, 0, 0, "扎帐金额 : " + decimalFormat.format(order.getPayAmount()) + " 元"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        //说明
        list.add(new PrintInfo(1, 0, 0, 1, "感谢您一天的辛勤付出"));
        list.add(new PrintInfo(1, 0, 0, 1, "祝您阖家欢乐,幸福安康"));
        //分割线
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "---------------------------------"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));

        return list;
    }


    private List<PrintInfo> getExitPrintInfos(TOrder order, TVehicleEntranceRecord tvr) {
        TSubParking subParking = subParkingMapper.selectById(tvr.getSubParkingId());
        TParkingSpace parkingSpace = parkingSpaceMapper.selectById(tvr.getParkingSpaceId());
        TCollector collector = collectorMapper.selectById(tvr.getEnterCollectorId());


        long ms = tvr.getExitTime().getTime() - tvr.getEnterTime().getTime();
        long hours = ms / (1000 * 60 * 60);
        long minutes = (ms - hours * (1000 * 60 * 60)) / (1000 * 60);
        List<PrintInfo> list = new LinkedList<>();

        list.add(new PrintInfo(1, 1, 1, 1, "车辆驶出凭证"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "停车路段 : " + subParking.getName()));
        list.add(new PrintInfo(0, 0, 0, 0, "泊 位 号 : " + parkingSpace.getCode()));
        list.add(new PrintInfo(0, 0, 0, 0, "车 牌 号 : " + tvr.getCarNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, "会员类型 : "+ getMemberTypeNameByMemberId(tvr.getMemberId())));
        list.add(new PrintInfo(0, 0, 0, 0, "员 工 号 : " + collector.getJobNumber()));
        list.add(new PrintInfo(0, 0, 0, 0, "驶入时间 : " + DateUtil.formatStandardDate(tvr.getEnterTime())));
        list.add(new PrintInfo(0, 0, 0, 0, "驶出时间 : " + DateUtil.formatStandardDate(tvr.getExitTime())));
        list.add(new PrintInfo(0, 0, 0, 0, "停车时长 : " + hours + "小时" + minutes + "分钟"));
        list.add(new PrintInfo(0, 0, 0, 0, "应缴金额 : " + decimalFormat.format(tvr.getChargeAmount()) + " 元"));
        list.add(new PrintInfo(0, 0, 0, 0, "优惠金额 : " + decimalFormat.format(tvr.getChargeAmount().subtract(tvr.getRealChargeAmount())) + " 元"));
        list.add(new PrintInfo(0, 0, 0, 0, "实缴车费 : " + decimalFormat.format(tvr.getRealChargeAmount())));
        if (order == null) {
            Assert.isTrue(tvr.getRealChargeAmount().compareTo(BigDecimal.ZERO) == 0, "不合法的订单打印请求");
            list.add(new PrintInfo(0, 0, 0, 0, "欠费补缴 : 0.00 元"));
            list.add(new PrintInfo(0, 0, 0, 0, "支付方式 : 免费"));
            list.add(new PrintInfo(0, 0, 0, 0, "支付金额 : 0.00 元"));
        } else {
            list.add(new PrintInfo(0, 0, 0, 0, "欠费补缴 : " + decimalFormat.format(order.getPayAmount().subtract(tvr.getRealChargeAmount())) + " 元"));
            list.add(new PrintInfo(0, 0, 0, 0, "支付方式 : " + PayWayEnum.getPayWayEnum(order.getPayWay()).getName()));
            list.add(new PrintInfo(0, 0, 0, 0, "支付金额 : " + decimalFormat.format(order.getPayAmount()) + " 元"));
        }
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, " "));

        //说明
        list.add(new PrintInfo(0, 0, 0, 0, "如您对本次收费存在疑问，欢迎拨打监督电话:0550-66666666(安徽谷声科技有限公司) 12580(物价局)"));

        //分割线
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        list.add(new PrintInfo(0, 0, 0, 0, "---------------------------------"));
        list.add(new PrintInfo(0, 0, 0, 0, " "));
        return list;
    }


    private String getMemberTypeNameByMemberId(Long memberId){
        if(null == memberId){
            return "临时车辆";
        }
        TMember member = memberMapper.selectById(memberId);
        TMemberType memberType = memberTypeMapper.selectById(member.getMemberTypeId());
        return memberType.getName();
    }

}
