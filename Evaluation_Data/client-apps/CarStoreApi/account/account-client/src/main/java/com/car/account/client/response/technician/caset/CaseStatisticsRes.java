package com.car.account.client.response.technician.caset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 案例统计
 * @author zhangyp
 * @date 2021/1/29 0:40
 */
@Data
@ApiModel
public class CaseStatisticsRes {

    @ApiModelProperty("案例uuid")
    private String caseUuid;

    @ApiModelProperty("案例名字")
    private String caseTitle;

    @ApiModelProperty("收益")
    private BigDecimal caseAmt;

    @ApiModelProperty("销量")
    private Integer saleNum;


}
