package com.car.order.client.request.order.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 下单
 * @author zhangyp
 * @date 2021/1/15 0:22
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class CreateOrderReq extends PreOrderReq{

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

    @Length(max = 512)
    @ApiModelProperty(value = "订单备注")
    private String remark;
}
