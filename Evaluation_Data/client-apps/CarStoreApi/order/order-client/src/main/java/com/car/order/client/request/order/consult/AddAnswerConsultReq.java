package com.car.order.client.request.order.consult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/3
 */
@Data
@ApiModel(value="AddAnswerConsultReq",description="技师回答咨询请求VO对象")
public class AddAnswerConsultReq {

    @NotBlank(message = "请输入订单uuid！")
    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @NotBlank(message = "请输入回答内容！")
    @ApiModelProperty(value = "回答内容",name = "answerContent")
    private String answerContent;

    @ApiModelProperty(value = "回答内容相关图片",name = "answerImgList")
    private List<String> answerImgList;
}
