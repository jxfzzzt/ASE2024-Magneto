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
public interface OrderFrontFeign {

    /**
     * 查询我的提问数量
     * @param userUuid
     * @return
     */
    @GetMapping(value = "/orderFront/queryQuizCount/{userUuid}")
    ResultRes<Integer> queryQuizCount(@PathVariable(name = "userUuid") String userUuid);


    /**
     * 查询我的案例数量
     * @param userUuid
     * @return
     */
    @GetMapping(value = "/orderFront/queryCaseCount/{userUuid}")
    ResultRes<Integer> queryCaseCount(@PathVariable(name = "userUuid") String userUuid);
}
