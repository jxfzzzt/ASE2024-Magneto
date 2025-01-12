package com.car.eureka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;


/**
 * @author HeLing
 * @since 2020-10-20
 */
@Configuration
@Slf4j
public class InstanceCancelListener implements ApplicationListener<EurekaInstanceCanceledEvent> {
    @Override
    public void onApplicationEvent(@NonNull EurekaInstanceCanceledEvent event) {
        log.info("服务:{}挂了",event.getAppName());
    }
}
