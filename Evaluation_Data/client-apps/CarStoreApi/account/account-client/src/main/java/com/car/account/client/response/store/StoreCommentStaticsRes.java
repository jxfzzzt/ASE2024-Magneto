package com.car.account.client.response.store;

import com.car.account.client.response.comment.CommentStaticsRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 店铺评论信息
 * @author zhangyp
 * @date 2021/1/17 17:27
 */
@Data
@ApiModel
public class StoreCommentStaticsRes extends CommentStaticsRes {

    @ApiModelProperty("商铺uuid")
    private String storeUuid;

    @ApiModelProperty("技术评分")
    private BigDecimal technologyScore;

    @ApiModelProperty("服务评分")
    private BigDecimal serviceScore;

    @ApiModelProperty("环境评分")
    private BigDecimal environmentScore;

}
