package com.car.order.client.feign;

import com.car.common.res.PageRes;
import com.car.order.client.request.order.goods.QueryOrderGoodsFrontListReq;
import com.car.order.client.response.order.goods.OrderGoodsFrontListRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-04-19 14:28
 */
@FeignClient(value = "order")
public interface OrderGoodsFrontFeign {

    /**
     *
     * @param queryStoreListReq
     * @return
     */
    @PostMapping(value = "/orderGoodsFront/queryOrderGoodsList")
    public PageRes<List<OrderGoodsFrontListRes>> queryOrderGoodsList(@RequestBody @Valid QueryOrderGoodsFrontListReq queryStoreListReq);
}
