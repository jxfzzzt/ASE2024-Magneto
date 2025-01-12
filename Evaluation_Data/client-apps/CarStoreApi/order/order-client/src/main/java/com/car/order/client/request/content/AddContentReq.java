package com.car.order.client.request.content;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/28
 */
@Data
@ApiModel(value="AddContentReq",description="新增内容请求VO")
public class AddContentReq {


    @NotBlank(message = "请输入订单uuid！")
    @ApiModelProperty(value = "订单uuid",name = "orderUuid")
    private String orderUuid;

    @NotBlank(message = "请输入订单名称！")
    @ApiModelProperty(value = "订单名称",name = "orderName")
    private String orderName;

    @NotNull(message = "请输入用户订单类型！")
    @ApiModelProperty(value = "订单分类:0 订单点评  1 提问 2 回答 3 案例",name = "orderType")
    private Integer orderType;

    @NotBlank(message = "请输入用户uuid！")
    @ApiModelProperty(value = "用户uuid",name = "userUuid")
    private String userUuid;

    @NotBlank(message = "请输入手机号！")
    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @NotEmpty(message = "请输入相关资源！")
    @ApiModelProperty(value = "资源列表",name = "resourcesList")
    private List<String> resourcesList;

    @NotBlank(message = "请输入具体内容！")
    @ApiModelProperty(value = "具体内容",name = "contentDetail")
    private String contentDetail;
}
