package com.car.order.client.response.order.consult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Data
@ApiModel(value="ConsultOrderFrontRes",description="付费咨询订单内容信息VO")
public class ConsultOrderFrontRes {

    @ApiModelProperty(value = "订单uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号", name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "咨询uuid",name = "consultUuid")
    private String consultUuid;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private String orderSts;

    @ApiModelProperty(value = "咨询标题",name = "title")
    private String title;

    @ApiModelProperty(value = "答复状态 0 未答复 1 已答复",name = "answerSts")
    private Integer answerSts;

    @ApiModelProperty(value = "答复时间",name = "answerTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date answerTime;

    @ApiModelProperty(value = "咨询描述",name = "consultDesc")
    private String consultDesc;

    @ApiModelProperty(value = "咨询图片",name = "consultImgUrlList")
    private List<String> consultImgUrlList;

    @ApiModelProperty(value = "回答描述", name = "answerDesc")
    private String answerDesc;

    @ApiModelProperty(value = "订单金额", name = "orderAmount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "回答图片",name = "answerUrlList")
    private List<String> answerUrlList;

    @ApiModelProperty(value = "订单类型 0 订单点评  1 咨询 2 回答 3 案例 4 旁听",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "技师uuid", name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "技师名称", name = "technicianName")
    private String technicianName;

    @ApiModelProperty(value = "技师手机号", name = "technicianMobile")
    private String technicianMobile;

    @ApiModelProperty(value = "技师头像", name = "technicianImgUrl")
    private String technicianImgUrl;
}
