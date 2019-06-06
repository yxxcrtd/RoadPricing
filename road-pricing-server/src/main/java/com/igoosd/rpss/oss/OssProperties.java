package com.igoosd.rpss.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 2018/3/13.
 */
@Configuration
@ConfigurationProperties(prefix = "oss")
@Data
public class OssProperties {

    private String endpoint;
    // 其他

    private int verPictureNumber;//停车图片上传数

    private final Bucket bucket = new Bucket();

    @Data
    public static class Bucket{
        private String verPicture;
    }
}
