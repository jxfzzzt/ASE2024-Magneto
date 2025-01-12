package com.car.account.web.common.utils;

import brave.Tracer;
import brave.propagation.ExtraFieldPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author linjiang.xie
 * @date 2019/9/18 16:32
 */
@Component
@ConditionalOnProperty(
        value = {"spring.sleuth.enabled"},
        matchIfMissing = true
)
public class SleuthUtils implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(SleuthUtils.class);
    public static Tracer tracer;
    private ApplicationContext applicationContext;

    public SleuthUtils() {
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (this.applicationContext == event.getApplicationContext()) {
            Optional<Tracer> option = this.applicationContext.getBeansOfType(Tracer.class).values().stream().findFirst();
            if (option.isPresent()) {
                tracer = (Tracer)option.get();
            } else {
                logger.info("no zipkin set, can not provide bagagge.");
            }
        }

    }

    static String getBaggage(String key) {
        return tracer != null ? ExtraFieldPropagation.get(key) : null;
    }

    static void setBaggage(String key, String value) {
        if (tracer != null) {
            ExtraFieldPropagation.set(key, value);
        }

    }
}
