package com.car.order.client.response.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="CarOwnerInfoRes",description="订单车主信息VO")
public class CarOwnerInfoRes {

    @ApiModelProperty(value = "车主uuid",name = "carOwnerUuid")
    private String carOwnerUuid;

    @ApiModelProperty(value = "车主地区",name = "carOwnerArea")
    private String carOwnerArea;

    @ApiModelProperty(value = "车主姓名",name = "carOwnerName")
    private String carOwnerName;

    @ApiModelProperty(value = "车主手机号",name = "carOwnerMobile")
    private String carOwnerMobile;

    @ApiModelProperty(value = "支付宝账号",name = "alipayAccount")
    private String alipayAccount;
}
