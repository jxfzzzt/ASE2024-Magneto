package com.car.account.client.feign;


import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 * @date 2020/6/15 11:47
 */
@FeignClient(value = "test123")
public interface TestFeign {

    /**
     * 演示代码块
     */
    @GetMapping(value = "/test")
    ResultRes test();



}
