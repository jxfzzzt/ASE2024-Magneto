package com.car.account.client.response.profit;

import com.car.account.client.response.account.AccountRes;
import com.car.account.client.response.profit.sub.ProfitStreamClassify;
import com.car.account.client.response.profit.sub.ProfitStreamRes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 账户资金汇总
 * @author zhangyp
 * @date 2021/1/27 22:14
 */
@Data
@ApiModel
public class AccountAmtRes extends AccountRes {

    /*@ApiModelProperty("资金流水明细列表")
    private List<ProfitStreamRes> profitStreamResList;*/

    @ApiModelProperty("资金收入分类统计")
    private List<ProfitStreamClassify> profitStreamClassifies;

}
