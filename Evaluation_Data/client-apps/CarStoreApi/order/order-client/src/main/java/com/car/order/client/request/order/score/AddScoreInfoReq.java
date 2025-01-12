package com.car.order.client.request.order.score;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Data
@ApiModel(value="AddScoreInfoReq",description="新增评分信息")
public class AddScoreInfoReq {

    @ApiModelProperty(value = "订单id", name = "orderUuid")
    private String orderUuid;

    @ApiModelProperty(value = "打分类型 1商品 2服务 3店铺", name = "scoreType")
    @NotNull(message = "请输入评分类型！")
    private Integer scoreType;

    @ApiModelProperty(value = "星值eg:4.5", name = "scoreStar")
    private BigDecimal scoreStar;

    @ApiModelProperty(value = "商品/服务/店铺uuid", name = "relationUuid")
    @NotEmpty(message = "商品/服务/店铺uuid！")
    private String relationUuid;

}
