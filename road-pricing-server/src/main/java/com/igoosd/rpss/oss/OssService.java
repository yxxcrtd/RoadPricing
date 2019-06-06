package com.igoosd.rpss.oss;

import java.io.InputStream;

/**
 * 2018/3/12.
 * 文件服务接口
 */
public interface OssService {



    /**
     * 保存停车记录图片
     * @param inputStream
     * @return
     */
    String saveVerPicture(Long verId,InputStream inputStream,String fileName);
}
