package com.igoosd.rpss.oss;

import com.igoosd.common.exception.RoadPricingException;
import com.igoosd.rpss.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 2018/3/13.
 * 资源服务 本地化存储方案
 * nginx 配置静态资源服务器 提供访问
 */
@Service
@Slf4j
public class OssServiceImpl implements OssService {

    @Autowired
    private OssProperties ossProperties;

    /**
     * 保存停车关联图片  返回图片下载的完整地址
     *
     * @param verId
     * @param inputStream
     * @return
     */
    @Override
    public String saveVerPicture(Long verId, InputStream inputStream,String fileName) {

        String verPicture = ossProperties.getBucket().getVerPicture();
        File file = new File("/home/file"+verPicture, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            while (true) {
                int c = inputStream.read(buf);
                fos.write(buf, 0, c);
                if (c < 1024) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RoadPricingException("保存停车记录图片IO异常", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return StringUtils.splitStr(verPicture, fileName);
    }
}
