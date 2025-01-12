package com.car.order.client.feign;

import com.car.common.res.PageRes;
import com.car.order.client.request.order.consult.QueryOrderConsultFrontListReq;
import com.car.order.client.request.order.instance.QueryOrderCaseFrontListReq;
import com.car.order.client.response.order.consult.OrderConsultFrontListRes;
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
public interface OrderConsultFrontFeign {
    /**
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/orderConsultFront/queryOrderConsultList")
    public PageRes<List<OrderConsultFrontListRes>> queryOrderConsultList(@RequestBody @Valid QueryOrderConsultFrontListReq param);
}
