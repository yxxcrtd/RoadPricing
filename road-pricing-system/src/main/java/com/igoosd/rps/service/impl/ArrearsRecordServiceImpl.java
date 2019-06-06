package com.igoosd.rps.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.igoosd.common.Assert;
import com.igoosd.common.enums.BizStatusEnum;
import com.igoosd.domain.ArrearsRecord;
import com.igoosd.mapper.ArrearsRecordMapper;
import com.igoosd.mapper.VehicleEntranceRecordMapper;
import com.igoosd.model.TArrearsRecord;
import com.igoosd.model.TVehicleEntranceRecord;
import com.igoosd.rps.service.ArrearsRecordService;
import com.igoosd.rps.util.AbsCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 2018/5/10.
 */
@Service
public class ArrearsRecordServiceImpl extends AbsCommonService<TArrearsRecord,Long> implements ArrearsRecordService {

    @Autowired
    private ArrearsRecordMapper arrearsRecordMapper;
    @Autowired
    private VehicleEntranceRecordMapper vehicleEntranceRecordMapper;


    @Override
    protected BaseMapper<TArrearsRecord> getMapper() {
        return arrearsRecordMapper;
    }

    @Override
    public Page<ArrearsRecord> fuzzyFindPage(ArrearsRecord arrearsRecord, Page<ArrearsRecord> page) {
        List<ArrearsRecord> list = arrearsRecordMapper.fuzzyFindPage(page,arrearsRecord);
        return page.setRecords(list);
    }


    @Transactional
    @Override
    public void wipeArrears(Long id) {
        TArrearsRecord tar = getEntityByKey(id);
        Assert.notNull(tar,"找不到指定的欠费记录");
        //欠费记录删除
        deleteByKey(id);

        TVehicleEntranceRecord ter = vehicleEntranceRecordMapper.selectById(tar.getVehicleEntranceId());
        Assert.notNull(ter,"数据异常，欠费记录关联的停车记录不存在");
        Assert.isTrue(BizStatusEnum.ARREARS_EXIT.getValue() == ter.getBizStatus(),"欠费记录关联的停车记录状态不为：欠费离场 状态");
        //同步更新停车记录
        TVehicleEntranceRecord terTemp = new TVehicleEntranceRecord();
        terTemp.setId(tar.getVehicleEntranceId());
        terTemp.setBizStatus(BizStatusEnum.MANUAL_WIPE_ARREARS.getValue());//人工干预 抹账

        vehicleEntranceRecordMapper.updateById(terTemp);
    }
}
