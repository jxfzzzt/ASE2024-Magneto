package com.car.order.client.response.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 咨询
 * @author zhangyp
 * @date 2021/1/29 3:17
 */
@Data
@ApiModel(value="ConsultBaseRes",description="咨询内容信息VO")
public class ConsultBaseRes {

    @ApiModelProperty(value = "咨询uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "咨询标题",name = "title")
    private String title;

    @ApiModelProperty(value = "技师姓名",name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "技师头像",name = "technicianImgUrl")
    private String technicianImgUrl;

    @ApiModelProperty(value = "车主姓名",name = "carOwnerName")
    private String carOwnerName;

    @ApiModelProperty(value = "咨询描述",name = "consultDesc")
    private String consultDesc;

    @ApiModelProperty(value = "回答描述",name = "answerDesc")
    private String answerDesc;

    @ApiModelProperty(value = "咨询时间",name = "createdTime")
    private String createdTime;

    @ApiModelProperty(value = "咨询金额",name = "consultAmt")
    private BigDecimal consultAmt;

    @ApiModelProperty(value = "订单uuid",name = "uuid")
    private String orderUuid;
}
