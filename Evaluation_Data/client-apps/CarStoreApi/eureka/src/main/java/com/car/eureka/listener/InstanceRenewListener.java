 package com.car.eureka.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

 /**
  * @author HeLing
  * @since 2020-10-20
  */

 @Configuration
 @Slf4j
 public class InstanceRenewListener implements ApplicationListener<EurekaInstanceRenewedEvent> {
     @Override
     public void onApplicationEvent(EurekaInstanceRenewedEvent event) {
         log.info("心跳检测服务：{}" ,event.getInstanceInfo().getAppName());
     }
 }
