package com.car.order.client.response.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value = "ConsultInfoListRes", description = "咨询列表信息VO")
public class ConsultInfoListRes {

	@ApiModelProperty(value = "订单uuid", name = "uuid")
	private String uuid;

	@ApiModelProperty(value = "咨询标题", name = "title")
	private String title;

	@ApiModelProperty(value = "技师姓名", name = "technicianName")
	private String technicianName;

	@ApiModelProperty(value = "技师头像地址", name = "technicianImgUrl")
	private String technicianImgUrl;

	@ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败", name = "orderSts")
	private Integer orderSts;

	@ApiModelProperty(value = "答复状态 0 未答复 1 已答复", name = "answerSts")
	private Integer answerSts;

	@ApiModelProperty(value = "车辆品牌", name = "vehicleBrand")
	private String vehicleBrand;

	@ApiModelProperty(value = "车型类型", name = "vehicleModel")
	private String vehicleModel;

//    @ApiModelProperty(value = "咨询描述",name = "consultDesc")
//    private String consultDesc;

}
