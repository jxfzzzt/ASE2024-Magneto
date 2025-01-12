package com.car.order.client.request.order.groupbuy;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class CreateOrderGroupbuyReq {

	@NotNull
	@Min(value = 0)
	@Max(value = 1)
	@ApiModelProperty("配送方式 0快递,1门店自取")
	private Integer receiveMethod;
	
	
	@NotNull
	@Min(value = 1)
	@Max(value = 1000)
	@ApiModelProperty(value = "购买数量")
	private Integer num;

	@Length(max = 200)
	@ApiModelProperty(value = "订单备注")
	private String remark;

}
