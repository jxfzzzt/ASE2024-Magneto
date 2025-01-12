package com.car.utility.web.common.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 常量对象
 * @author 谢林江
 *
 */
public class PayConstants {
    /**
     * 微信退款证书
     */
    public static final String WEIXIN_APICLIENT_CERT = "config/static/apiclient_cert.p12";

}
