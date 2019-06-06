package com.igoosd.rpss.service;

import com.igoosd.domain.LocationReport;

/**
 * 2018/4/11.
 */
public interface LocationReportService {

    /**
     * 位置上报
     * @param lr
     */
    void reportLocation(LocationReport lr);
}
