package com.igoosd.domain;

import com.igoosd.model.TAccountBalance;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 2018/3/30.
 */
@Data
@ToString(callSuper = true)
public class AccountBalance extends TAccountBalance {

    private Date startTime; //分页查询
    private Date endTime;
}
