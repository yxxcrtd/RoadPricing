package com.igoosd.rpss.vo;

import lombok.Data;

import java.util.List;

/**
 * 2018/2/28.
 * vehicleEntranceRecordId、 arrearsRecordIds 没有必然联系
 * 不同的变量对应不同的业务场景
 * 当都有值存在时 表明 用户在聚合支付（出场缴费的同时合并进行了欠费补缴）
 */
@Data
public class BusinessInfo {

    private Long vehicleEntranceRecordId; //停车出入记录ID

    private List<Long> arrearsRecordIds;

    private  Long accountSummaryId;

}
