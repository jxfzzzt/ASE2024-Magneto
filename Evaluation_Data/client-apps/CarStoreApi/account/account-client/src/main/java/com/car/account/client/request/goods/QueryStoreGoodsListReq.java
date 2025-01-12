package com.car.account.client.request.goods;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author zhangyp
 * @date 2021/1/17 0:40
 */
@Data
@ApiModel
public class QueryStoreGoodsListReq extends PageReq {

    @ApiModelProperty("销售状态:0库存(下架)1在售(上架)")
    @Max(value = 1)
    @Min(value = 0)
    private Integer sellSts;
}
