package com.car.system.web.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Buerger
 * @date 2020/10/22 17:44
 */
@Data
@ConfigurationProperties("system")
public class PlantFormProperties {

    private boolean singleLogin;
}
