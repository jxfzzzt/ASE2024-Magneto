package com.car.gateway;

import com.car.common.annotation.EnableRedisCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author xlj
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.car"})
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@EnableRedisCache
@EnableSwagger2
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }


}

