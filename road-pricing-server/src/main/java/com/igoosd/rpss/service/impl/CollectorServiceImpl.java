package com.igoosd.rpss.service.impl;

import com.igoosd.common.Assert;
import com.igoosd.common.enums.JobStatusEnum;
import com.igoosd.common.util.HashKit;
import com.igoosd.domain.Collector;
import com.igoosd.mapper.CollectorMapper;
import com.igoosd.model.TCollector;
import com.igoosd.rpss.service.CollectorService;
import com.igoosd.rpss.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 2018/2/5.
 */
@Service
@Slf4j
public class CollectorServiceImpl implements CollectorService {

    @Autowired
    private CollectorMapper collectorMapper;

    @Override
    public TCollector loginVerify(String jobNumber, String password) {
        TCollector tc = new TCollector();
        tc.setJobNumber(jobNumber);
        tc.setJobStatus(JobStatusEnum.On_JOB.getValue());
        TCollector collector = collectorMapper.selectOne(tc);
        if(null != collector){
            if(collector.getPassword().equals(HashKit.md5(password))){
                collector.setPassword(null);
                return collector;
            }
        }
        return null;
    }

    @Override
    public void modifyPwd(String oldPwd, String newPwd) {
        TCollector  collector = collectorMapper.selectById(LoginUser.get().getCollectorId());
        Assert.isTrue(HashKit.md5(oldPwd).equals(collector.getPassword()),"输入的旧密码错误");
        Collector temp = new Collector();
        temp.setPassword(HashKit.md5(newPwd));
        temp.setId(collector.getId());
        collectorMapper.updateById(temp);

    }

    @Override
    public TCollector getCollectorById(Long id) {
        return collectorMapper.selectById(id);
    }
}
