package com.igoosd.rpss.service.impl;

import com.igoosd.domain.LocationReport;
import com.igoosd.mapper.LocationReportMapper;
import com.igoosd.rpss.service.LocationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/4/11.
 */
@Service
public class LocationReportServiceImpl implements LocationReportService {

    @Autowired
    private LocationReportMapper locationReportMapper;

    @Override
    public void reportLocation(LocationReport lr) {
        locationReportMapper.insert(lr);
    }
}
