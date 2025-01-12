package com.car.order.client.request.order.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/8
 */
@Data
@ApiModel(value="AddOrderInfoReq",description="新增订单info信息")
public class AddOrderInfoReq {

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单类型1.维修,2.咨询,3.案例,4.代驾,5.维修,6.代驾，7.dtc故障，8.课程 ,9现场下单, 10：共享技师", name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    private String orderUuid;
}
