package com.car.account.client.response.profit.sub;

import com.car.account.client.request.profit.ProfitStreamReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangyp
 * @date 2021/1/27 21:43
 */
@Data
@ApiModel
public class ProfitStreamRes extends ProfitStreamReq {

    /**
     * 金额
     */
    @ApiModelProperty(value="金额",name="amt")
    private BigDecimal amt;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注",name="remarks")
    private String remarks;

    @ApiModelProperty(value = "创建时间",name = "createTime",example = "yyyy-MM-dd HH:mi:ss")
    private String createTime;
}
