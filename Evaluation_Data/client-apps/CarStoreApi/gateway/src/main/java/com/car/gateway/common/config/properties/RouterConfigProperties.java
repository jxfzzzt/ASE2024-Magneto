package com.car.gateway.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HeLing
 * @since 2020-10-23
 */

@Data
public class RouterConfigProperties {

    /**
     * 放行的url
     */
    private List<String> noFilterUrl;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;

}
