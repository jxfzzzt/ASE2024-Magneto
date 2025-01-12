package com.car.order.client.feign;

import com.car.common.res.PageRes;
import com.car.order.client.request.order.consult.QueryOrderConsultFrontListReq;
import com.car.order.client.request.technicianappointment.QueryShareTechnicianOrderReq;
import com.car.order.client.response.order.consult.OrderConsultFrontListRes;
import com.car.order.client.response.technicianappointment.ShareTechnicianOrderRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
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
public interface ShareTechnicianFrontFeign {

    @PostMapping(value = "/shareTechnicianOrder/queryShareTechnicianOrderList")
    public PageRes<List<ShareTechnicianOrderRes>> queryShareTechnicianOrderList(@RequestBody @Validated QueryShareTechnicianOrderReq req);
}
