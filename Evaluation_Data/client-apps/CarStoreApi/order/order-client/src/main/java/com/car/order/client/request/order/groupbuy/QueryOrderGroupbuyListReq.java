package com.car.order.client.request.order.groupbuy;

import java.util.List;

import com.car.common.req.PageReq;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class QueryOrderGroupbuyListReq extends PageReq {

	@ApiModelProperty("主键")
	private String uuid;

	@ApiModelProperty("0待支付；1待成团；2待预约；3取消待审核；4已取消；5待上门；6上门待确认；7待使用；8完成待确认；9已完成")
	private List<Integer> orderSts;

	@ApiModelProperty("团购uuid")
	private String groupbuyUuid;

	@ApiModelProperty("购买人")
	private String userUuid;

	@ApiModelProperty("订单编号")
	private String orderNum;

	@ApiModelProperty("支付方式 0 微信支付 1 支付宝支付")
	private List<Integer> payType;

	@ApiModelProperty("团配送方式 0快递,1到店服务,2上门服务")
	private List<Integer> receiveMethod;

	@ApiModelProperty("团状态：0=待开始;1=进行中;2=已结束")
	private List<Integer> groupSts;

	@ApiModelProperty("团的创建人id：服务提供方Id")
	private String groupUserUuid;
}
