package com.igoosd.rpss.service;

import com.igoosd.model.TAccountSummary;

/**
 * 2018/3/2.
 * 扎帐接口
 */
public interface AccountSummaryService {

    boolean allowLoginForAccountSummary(Long collectorId);

    /**
     * 获取收费员当前最新的扎帐信息记录
     * @param collectorId
     * @return
     */
    TAccountSummary getLatestAccountSummaryRecordByCollectorId(Long collectorId);
}
