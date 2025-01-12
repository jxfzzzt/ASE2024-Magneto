package com.car.order.client.feign;

import com.car.common.res.PageRes;
import com.car.order.client.request.order.instance.QueryOrderCaseFrontListReq;
import com.car.order.client.response.order.instance.OrderCaseFrontListRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-19 14:35
 */
@FeignClient(value = "order")
public interface OrderCaseFrontFeign {
    /**
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/orderCaseFront/queryOrderCaseList")
    public PageRes<List<OrderCaseFrontListRes>> queryOrderCaseList(@RequestBody @Valid QueryOrderCaseFrontListReq param);
}
