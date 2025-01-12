package com.car.account.client.response.withdrawal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@ApiModel(value="QueryWithdrawalListRes",description="提现列表VO对象")
@Data
public class QueryWithdrawalListRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户 uuid (店铺、技师等)",name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value = "用户名称或店铺名称",name = "userName")
    private String userName;

    @ApiModelProperty(value = "提现用户角色 0 店铺 1 技师",name = "userRole")
    private Integer userRole;

    @ApiModelProperty(value = "手机号码",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "创建时间",name = "createdTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "审核时间",name = "lastUpdatedTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedTime;

    @ApiModelProperty(value = "提现金额",name = "withdrawalAmount")
    private BigDecimal withdrawalAmount;

    @ApiModelProperty(value = "审核状态 (0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;
}
