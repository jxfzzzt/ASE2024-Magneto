package com.car.order.client.request.comment;

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
 * Date:  2021/1/27
 */
@Data
@ApiModel(value="QueryCommentListReq",description="查询评论信息列表请求VO")
public class QueryCommentInfoListReq extends PageReq {

    @ApiModelProperty(value = "商品/服务/店铺uuid", name = "relationUuid")
    private String relationUuid;

    @ApiModelProperty(value = "评论类型1商品2服务3店铺", name = "scoreType")
    private Integer scoreType;
}
