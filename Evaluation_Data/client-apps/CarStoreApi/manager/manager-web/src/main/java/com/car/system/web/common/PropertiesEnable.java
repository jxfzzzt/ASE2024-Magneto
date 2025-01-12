package com.car.system.web.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Buerger
 * @date 2020/10/22 17:46
 */
@Configuration
@EnableConfigurationProperties(value = PlantFormProperties.class)
public class PropertiesEnable {
}
