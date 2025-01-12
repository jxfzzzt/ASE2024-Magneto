package com.car.gateway.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Buerger
 * @date 2020/10/22 17:44
 */
@Data
@ConfigurationProperties("platform")
public class PlantFormProperties {

    private boolean enablePermissionVerify;

    private RouterConfigProperties routerConfigProperties;
}
