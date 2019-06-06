package com.igoosd.rpss.pay.union.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 2018/3/21.
 */
@Configuration
@ConfigurationProperties(prefix = "union")
@Data
public class UnionProperties {

    private String cusid;//商户号

    private String appid;//appid

    private String key;//key

    private String domain;

    private String notifyUrl;//异步通知url

    private String version;
}

