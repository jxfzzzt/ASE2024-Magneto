package com.car.order.client.response.order.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="CaseInfoRes",description="案例信息VO")
public class CaseInfoRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @ApiModelProperty(value = "案例uuid",name = "caseUuid")
    private String caseUuid;

    @ApiModelProperty(value = "案例名称",name = "caseName")
    private String caseName;

    @ApiModelProperty(value = "案例数量",name = "caseNum")
    private Integer caseNum;

    @ApiModelProperty(value = "案例资源地址",name = "caseImgUrl")
    private String caseImgUrl;

    @ApiModelProperty(value = "案例价格",name = "materialsExpenses")
    private BigDecimal materialsExpenses;
}
