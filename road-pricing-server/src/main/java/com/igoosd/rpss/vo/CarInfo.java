package com.igoosd.rpss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/2/7.
 */
@Data
public class CarInfo {

    private String carNumber;//车牌号

    private Long memberTypeId;//会员类型ID

    private String memberTypeName;//会员身份标志

    @JsonFormat(pattern ="yyyy-MM-dd")
    private Date startDate;//会员开始时间

    @JsonFormat(pattern ="yyyy-MM-dd")
    private Date endDate;//会员结束时间

    private BigDecimal totalArrearsAmount;//总欠费金额

    private String parkingSpaceCode;

    private String parkingName;

    private String parkingAddress;

    private BigDecimal chargeAmount;  //应收

    private BigDecimal realChargeAmount;//实收

    private Date  preExitTime; //预出场时间

    private Date enterTime;//入场时间

    private Date exitTime;//出场时间

    List<String> pictureUrls;

    private Long EnterCollectorId;
    private String enterColJobNumber;
    private Long exitCollectorId;
    private String exitColJobNumber;

    private Long vehicleEntranceRecordId;


}
