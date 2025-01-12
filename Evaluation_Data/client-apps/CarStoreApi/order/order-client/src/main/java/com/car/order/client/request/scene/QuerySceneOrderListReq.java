package com.car.order.client.request.scene;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/5
 */
@Data
@ApiModel(value = "QuerySceneOrderListReq", description = "查询现场订单列表详情VO")
public class QuerySceneOrderListReq extends PageReq {

    @ApiModelProperty(value = "查询现场订单类型，0：未抢订单，1：已抢订单，2：发布订单", name = "queryType")
    private Integer queryType;
}
