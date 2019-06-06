package com.igoosd.rpss.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.util.DateUtil;
import com.igoosd.domain.DutyStatistics;
import com.igoosd.mapper.DutyRecordMapper;
import com.igoosd.mapper.DutyStatisticsMapper;
import com.igoosd.mapper.LocationReportMapper;
import com.igoosd.model.TDutyRecord;
import com.igoosd.model.TLocationReport;
import com.igoosd.rpss.service.ConfigService;
import com.igoosd.rpss.util.RpssConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 2018/3/16.
 */
@Service
@Slf4j
@Order(3)
public class DutyStatisticsTask extends AbsTask {

    @Autowired
    private LocationReportMapper locationReportMapper;
    @Autowired
    private DutyRecordMapper dutyRecordMapper;
    @Autowired
    private ConfigService configService;
    @Autowired
    private DutyStatisticsMapper dutyStatisticsMapper;

    @Scheduled(cron = "0 30 22 * * *")
    public void doTask() {
        if (!preTask()) {
            return;
        }
        log.info("---------收费员考勤统计开始---------");
        //业务操作
        Date curDate = DateUtil.convertYmdDate(new Date());
        Date nextDate = DateUtil.getNextDate(curDate);
        List<Long> collectorIds = dutyRecordMapper.getCurDateDutyCollectorIds();
        for (Long collectorId : collectorIds) {
            int count = locationReportMapper.selectCount(new EntityWrapper<TLocationReport>()
                    .between("report_time", curDate, nextDate).eq("collector_id", collectorId)
                    .eq("allow", true));
            int reportTimes = Integer.parseInt(configService.getValue(RpssConst.cfg_key_report_times));
            double hours = reportTimes * count/60.0;
            List<TDutyRecord> list = dutyRecordMapper.selectList(new EntityWrapper<TDutyRecord>().eq("collector_id",collectorId)
                    .orderBy("login_time",false));

            DutyStatistics dutyStatistics = new DutyStatistics();
            dutyStatistics.setCollectorId(collectorId);
            dutyStatistics.setCreateTime(new Date());
            dutyStatistics.setDutyDate(curDate);
            dutyStatistics.setOnlineTotalTime(hours);
            dutyStatistics.setSubParkingId(list.get(0).getSubParkingId());
            dutyStatistics.setLatestOnDutyTime(list.get(0).getLoginTime());
            dutyStatistics.setLatestOffDutyTime(list.get(0).getLogoutTime());
            dutyStatisticsMapper.insert(dutyStatistics);
        }
        log.info("---------收费员考勤统计完成---------");
    }
}
