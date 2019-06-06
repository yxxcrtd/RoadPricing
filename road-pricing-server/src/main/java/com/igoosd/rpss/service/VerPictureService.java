package com.igoosd.rpss.service;

import java.io.InputStream;
import java.util.List;

/**
 * 2018/3/13.
 */
public interface VerPictureService {


    /**
     * 获取停车记录关联的图片完整URL列表
     * @param verId
     * @return
     */
    List<String> getDetailPictureUrlsForVerId(Long verId);


    /**
     * 上传停车关联图片
     * @param verId
     * @param inputStream
     * @return
     */
    String uploadPicture(Long verId, InputStream inputStream);
}
