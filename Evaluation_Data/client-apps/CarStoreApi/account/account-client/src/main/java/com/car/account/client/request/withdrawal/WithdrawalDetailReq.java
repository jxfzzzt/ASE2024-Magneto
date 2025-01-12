package com.car.account.client.request.withdrawal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@ApiModel(value="WithdrawalDetailReq",description="新增提现详情请求VO")
public class WithdrawalDetailReq {

    @ApiModelProperty(value = "代驾金额",name = "drivingAmount")
    private BigDecimal drivingAmount;

    @ApiModelProperty(value = "维修金额",name = "serviceAmount")
    private BigDecimal serviceAmount;

    @ApiModelProperty(value = "现场支持金额",name = "supportAmount")
    private BigDecimal supportAmount;

    @ApiModelProperty(value = "案例金额",name = "caseAmount")
    private BigDecimal caseAmount;

    @ApiModelProperty(value = "回答金额",name = "qaAmount")
    private BigDecimal qaAmount;
}
