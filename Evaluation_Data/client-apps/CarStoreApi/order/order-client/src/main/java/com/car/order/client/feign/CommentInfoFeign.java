package com.car.order.client.feign;

import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author zhangyp
 * @date 2021/1/31 15:31
 */
@FeignClient(value = "order")
public interface CommentInfoFeign {

    @GetMapping(value = "/comment/queryGoodsCommentCount/{uuid}")
    ResultRes<Integer> queryGoodsCommentCount(@PathVariable(name = "uuid") String uuid);
}
