package com.car.account.client.response.withdrawal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@Data
@ApiModel(value="WithdrawalDetailRes",description="提现详情VO")
public class WithdrawalDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "提现数据ID",name = "withdrawalUuid")
    private String withdrawalUuid;

    @ApiModelProperty(value = "数据类型 1:提现  2:待入账",name = "type")
    private Integer withdrawalType;

    @ApiModelProperty(value = "代驾金额",name = "drivingAmount")
    private BigDecimal drivingAmount;

    @ApiModelProperty(value = "维修金额",name = "serviceAmount")
    private BigDecimal serviceAmount;

    @ApiModelProperty(value = "现场支持金额",name = "supportAmount")
    private BigDecimal supportAmount;

    @ApiModelProperty(value = "回答金额",name = "qaAmount")
    private BigDecimal qaAmount;
}
