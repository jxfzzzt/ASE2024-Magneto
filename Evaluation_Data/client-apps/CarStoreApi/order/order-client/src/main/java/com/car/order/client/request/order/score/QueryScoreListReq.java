package com.car.order.client.request.order.score;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Data
@ApiModel(value = "QueryScoreListReq", description = "查询评分列表请求VO")
public class QueryScoreListReq extends PageReq {

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    private String orderUuid;

    @ApiModelProperty(value = "打分类型1商品2服务3店铺", name = "scoreType")
    private Integer scoreType;

    @ApiModelProperty(value = "商品/服务/店铺uuid", name = "relationUuid")
    private String relationUuid;
}
