package com.car.order.client.request.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Data
@ApiModel(value="AddCommentReq",description="新增评论请求VO")
public class AddCommentReq {

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    @NotEmpty(message = "请输入订单uuid！")
    private String orderUuid;

    @ApiModelProperty(value = "评论详情", name = "commentDesc")
    @NotEmpty(message = "请输入评论信息！")
    private String commentDesc;

    @ApiModelProperty(value = "用户手机号", name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "商品/服务/店铺uuid", name = "relationUuid")
    @NotEmpty(message = "请输入商品/服务/店铺uuid！")
    private String relationUuid;

    @ApiModelProperty(value = "评论类型1商品2服务3店铺", name = "scoreType")
    @NotNull(message = "请输入评论类型！")
    private Integer scoreType;

}
