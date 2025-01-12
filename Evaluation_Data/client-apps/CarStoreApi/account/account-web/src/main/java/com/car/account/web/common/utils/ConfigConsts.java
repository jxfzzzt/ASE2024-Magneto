package com.car.account.web.common.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;

/**
 * 常量对象
 *
 * @author 谢林江
 */
@Component
@Data
@ConfigurationProperties(prefix = "config")
public class ConfigConsts {

    /**
     * 短信开关 close：默认发送888888  open:动态
     */
    private String smsSwitch;


}
