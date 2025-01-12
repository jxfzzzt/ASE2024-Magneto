package com.car.order.client.request.order.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/26
 */
@Data
@ApiModel(value="QueryOrderStsNumReq",description="查询订单状态数量请求VO对象")
public class QueryOrderStsNumReq {

    @ApiModelProperty(value = "订单类型 0 维修保养， 1 线上咨询， 2 案例 10：共享工位")
    @NotNull(message = "订单类型不能为空")
    private Integer orderType;

}
