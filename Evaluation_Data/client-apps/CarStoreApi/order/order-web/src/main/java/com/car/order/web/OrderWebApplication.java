package com.car.order.web;

import com.car.common.annotation.EnableRedisCache;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author xlj
 */
@EnableRedisCache
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.car.*.web.mapper")
@EnableFeignClients(basePackages = "com.car.*.client.feign")
@EnableSwagger2
@EnableDistributedTransaction
public class OrderWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderWebApplication.class, args);
    }

}
