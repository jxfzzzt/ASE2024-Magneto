package com.car.order.client.response.order.instance;

import com.car.order.client.response.order.driving.AfterSaleInfoRes;
import com.car.order.client.response.order.CarOwnerInfoRes;
import com.car.order.client.response.order.TechnicianInfoRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="OrderCaseDetailRes",description="案例详情信息VO")
public class OrderCaseDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评",name = "evaluateSts")
    private Integer evaluateSts;

    @ApiModelProperty(value = "技师评分",name = "technicianScore")
    private BigDecimal technicianScore;

    @ApiModelProperty(value = "售后信息",name = "afterSaleInfoRes")
    private AfterSaleInfoRes afterSaleInfoRes;

    @ApiModelProperty(value = "车主信息",name = "carOwnerInfoRes")
    private CarOwnerInfoRes carOwnerInfoRes;

    @ApiModelProperty(value = "订单信息",name = "orderDetailRes")
    private OrderDetailRes orderDetailRes;

    @ApiModelProperty(value = "技师(代驾)信息",name = "technicianInfoRes")
    private TechnicianInfoRes technicianInfoRes;

    @ApiModelProperty(value = "案例信息列表",name = "technicianInfoRes")
    private List<CaseInfoRes> caseInfoListRes;
}
