package com.car.account.client.request.groupbuy;

import java.util.List;

import com.car.common.req.PageReq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询团购条件")
public class QueryGroupbuyListReq extends PageReq {

	@ApiModelProperty("状态：0=待开始;1=进行中;2=已结束")
	private List<Integer> groupSts;

	@ApiModelProperty("所属人id")
	private String userUuid;

	@ApiModelProperty("配送方式 0快递,1到店服务,2上门服务")
	private List<Integer> receiveMethod;

}
