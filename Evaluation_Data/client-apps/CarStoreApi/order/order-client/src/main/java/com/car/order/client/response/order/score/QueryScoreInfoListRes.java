package com.car.order.client.response.order.score;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/30
 */
@Data
@ApiModel(value = "QueryScoreInfoListRes", description = "评分信息")
public class QueryScoreInfoListRes extends PageReq {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单uuid", name = "orderUuid")
    private String orderUuid;

    @ApiModelProperty(value = "打分类型1商品2服务3店铺", name = "scoreType")
    private Integer scoreType;

    @ApiModelProperty(value = "星值eg:4.5", name = "scoreStar")
    private BigDecimal scoreStar;

    @ApiModelProperty(value = "评分用户", name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value = "商品/服务/店铺uuid", name = "relationUuid")
    private String relationUuid;



}
