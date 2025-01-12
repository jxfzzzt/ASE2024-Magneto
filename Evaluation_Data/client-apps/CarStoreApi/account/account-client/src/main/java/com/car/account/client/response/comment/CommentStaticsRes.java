package com.car.account.client.response.comment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangyp
 * @date 2021/1/17 14:46
 */
@Data
@ApiModel
public class CommentStaticsRes {


    @ApiModelProperty("总条数")
    private Integer totalNum;

    @ApiModelProperty("评分")
    private BigDecimal score;
}
