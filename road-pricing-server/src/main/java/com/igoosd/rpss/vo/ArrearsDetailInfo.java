package com.igoosd.rpss.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 2018/3/12.
 */
@Data
@ToString
public class ArrearsDetailInfo {

    private String carNumber;
    private String parkingSpaceCode;
    private String memberTypeName;
    private BigDecimal chargeAmount;
    private BigDecimal realChargeAmount;
    private int bizStatus;
    private String parkingName;
    private String parkingAddress;
    private String enterCollector;
    private String exitCollector;
    private Date enterTime;
    private Date exitTime;
    private List<String> carPictures;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date memberStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date memberEndDate;

}
