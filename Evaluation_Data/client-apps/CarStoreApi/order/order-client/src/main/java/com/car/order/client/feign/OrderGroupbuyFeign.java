package com.car.order.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;

import springfox.documentation.annotations.ApiIgnore;

@FeignClient(value = "order")
public interface OrderGroupbuyFeign {

	@GetMapping(value = "/orderGroupbuy/updateGroupbuyEnd/{groupbuyUuid}")
	@SysOperLog(operModul = "团购订单管理", operType = OperEnum.UPDATE, operDesc = "团购结束时订单处理")
	@ApiIgnore
	public ResultRes<String> updateGroupbuyEnd(@PathVariable(value = "groupbuyUuid") String groupbuyUuid);
}
