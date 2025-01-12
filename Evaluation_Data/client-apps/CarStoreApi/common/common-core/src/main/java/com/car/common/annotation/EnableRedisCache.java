package com.car.common.annotation;

import com.car.common.config.RedisCacheConfig;
import com.car.common.exception.MyExceptionHandler;
import com.car.common.swagger.SwaggerConfig;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.token.SleuthUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author xlj
 * @date 2020/11/4 17:24
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(value = {RedisCacheConfig.class, RedisUtils.class, SwaggerConfig.class, MyExceptionHandler.class, SleuthUtils.class})
public @interface EnableRedisCache {
}
