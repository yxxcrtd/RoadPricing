package com.igoosd.rpss.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.igoosd.common.Assert;
import com.igoosd.mapper.VerPictureMapper;
import com.igoosd.model.TVerPicture;
import com.igoosd.rpss.oss.OssProperties;
import com.igoosd.rpss.oss.OssService;
import com.igoosd.rpss.service.VerPictureService;
import com.igoosd.rpss.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 2018/3/13.
 */
@Service
@Slf4j
public class VerPictureServiceImpl implements VerPictureService{

    @Autowired
    private VerPictureMapper verPictureMapper;
    @Autowired
    private OssProperties ossProperties;
    @Autowired
    private OssService ossService;

    @Override
    public List<String> getDetailPictureUrlsForVerId(Long verId) {

        List<TVerPicture> list =  verPictureMapper.selectList(new EntityWrapper<TVerPicture>().eq("vehicle_entrance_record_id",verId).orderBy("order_num",true));
        List<String> rstList = new LinkedList<>();
        for (TVerPicture p : list){
            rstList.add(ossProperties.getEndpoint()+p.getPicturlUrl());
        }
        return rstList;
    }

    @Override
    public String uploadPicture(Long verId, InputStream inputStream) {


        int count = verPictureMapper.selectCount(new EntityWrapper<TVerPicture>().eq("vehicle_entrance_record_id", verId));
        Assert.isTrue(count < ossProperties.getVerPictureNumber(), "停车记录关联照片不允许超过{}张" +
                ",当前已存储{}张，本次操作拒绝", ossProperties.getVerPictureNumber(), count);
        String fileName = (count+1)+".png";

        String fileUrl = ossService.saveVerPicture(verId,inputStream,fileName);
        //数据库保存
        TVerPicture p = new TVerPicture();
        p.setVehicleEntranceRecordId(verId);
        p.setOrderNum(count + 1);
        p.setPicturlUrl(fileUrl);
        verPictureMapper.insert(p);
        log.info("文件下载地址:{}",fileUrl);
        return StringUtils.splitStr(ossProperties.getEndpoint(),fileUrl);
    }
}
