package com.igoosd.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author hurd@igoosd.com
 * @since 2018-04-10
 */
@Data
@ToString
@TableName("t_account_summary")
public class TAccountSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long collectorId;
    /**
     * 扎帐日期 yyyy-MM-dd
     */
    private Date summaryDate;
    /**
     * 总收入
     */
    private BigDecimal totalAmount;
    /**
     * 线上收入
     */
    private BigDecimal onlineAmount;
    /**
     * 线下收入（现金收入）
     */
    private BigDecimal offlineAmount;
    /**
     * 扎帐状态：0：带扎帐 1 线下扎帐2线上扎帐 3扎帐成功
     */
    private Integer summaryStatus;
    private Date createTime;
    private Date summaryTime;
    /**
     * 描述
     */
    private String remark;

}
