package com.car.eureka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;


/**
 * @author HeLing
 * @since 2020-10-20
 */
@Configuration
@Slf4j
public class InstanceRegisterListener implements ApplicationListener<EurekaInstanceRegisteredEvent> {
    @Override
    public void onApplicationEvent(@NonNull EurekaInstanceRegisteredEvent eurekaInstanceRegisteredEvent) {
        log.info("服务：{}，注册成功了",eurekaInstanceRegisteredEvent.getInstanceInfo().getAppName());
    }
}
