package com.car.utility.web.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量对象
 * @author 谢林江
 *
 */
@Component
@Data
@ConfigurationProperties(prefix="sms")
public class SmsConstants {

    /**
     * 账号Key
     */
    private String accessKeyID;
    /**
     * 账号Key证书
     */
    private String accessKeySecret;
    /**
     * 组织ID
     */
    private String regionId;

    /**
     * domain
     */
    private String domain;

    /**
     * version
     */
    private String version;

    /**
     * version
     */
    private String action;

    /**
     * 短信签名
     */
    private String signName;
}
