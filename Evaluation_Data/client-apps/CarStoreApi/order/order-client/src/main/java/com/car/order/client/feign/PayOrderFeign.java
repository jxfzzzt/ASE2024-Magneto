package com.car.order.client.feign;

import com.car.common.res.ResultRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cjw
 * @date 2021/1/31 15:18
 */
@FeignClient(value = "order")
public interface PayOrderFeign {

    /**
     * 修改订单支付状态
     * @param orderInfoUuid
     * @return
     */
    @GetMapping(value = "/pay/updateOrderPaySts/{orderInfoUuid}")
    ResultRes<String> updateOrderPaySts(@PathVariable(value = "orderInfoUuid") String orderInfoUuid);
}
