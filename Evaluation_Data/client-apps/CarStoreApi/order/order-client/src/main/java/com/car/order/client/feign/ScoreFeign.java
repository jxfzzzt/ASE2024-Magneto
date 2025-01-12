package com.car.order.client.feign;

import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhangyp
 * @date 2021/1/31 15:18
 */
@FeignClient(value = "order")
public interface ScoreFeign {
    @GetMapping(value = "/score/queryGoodsScore/{uuid}")
    ResultRes<String> queryGoodsScore(@PathVariable(name = "uuid") String uuid);
}
