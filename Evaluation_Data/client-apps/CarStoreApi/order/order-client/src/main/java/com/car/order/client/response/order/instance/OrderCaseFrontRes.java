package com.car.order.client.response.order.instance;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/31
 */
@Data
@ApiModel(value="OrderCaseFrontRes",description="案例订单信息VO")
public class OrderCaseFrontRes {

    @ApiModelProperty(value = "订单uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "案例uuid",name = "caseUuid")
    private String caseUuid;

    @ApiModelProperty(value = "案例标题",name = "title")
    private String title;

    @ApiModelProperty(value = "案例制造日期",name = "madeTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date madeTime;

    @ApiModelProperty(value = "故障现象", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "诊断思路和过程", name = "ideaProcess")
    private String ideaProcess;

    @ApiModelProperty(value = "结论总结")
    private String summary;

    @ApiModelProperty(value = "案例资源地址",name = "caseImgUrl")
    private List<String> caseImgUrl;

    @ApiModelProperty(value = "案例价格",name = "materialsExpenses")
    private BigDecimal materialsExpenses;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

}
