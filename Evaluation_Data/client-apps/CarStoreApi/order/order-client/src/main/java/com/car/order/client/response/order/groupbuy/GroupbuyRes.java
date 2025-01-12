package com.car.order.client.response.order.groupbuy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.car.order.client.response.order.goods.GoodsRes;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "团购VO")
public class GroupbuyRes {

	@ApiModelProperty("uuid")
	private String uuid;

	@ApiModelProperty("售价")
	private BigDecimal price;

	@ApiModelProperty("成团数量")
	private Integer userNum;

	@ApiModelProperty("团购开始时间")
	private Date startTime;

	@ApiModelProperty("团购结束时间")
	private Date endTime;

	@ApiModelProperty("当前参与人数")
	private Integer participateNum;

	@ApiModelProperty("所属人id")
	private String userUuid;

	@ApiModelProperty("状态：0=待开始;1=进行中;2=已结束")
	private Integer groupSts;

	@ApiModelProperty("成团时间")
	private Date intoGroupTime;

	@ApiModelProperty("配送方式 0快递,1到店服务,2上门服务")
	private Integer receiveMethod;

	@ApiModelProperty("商品集合")
	private List<GoodsRes> goodsRes;
}
