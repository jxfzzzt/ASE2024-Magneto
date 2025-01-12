package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Data
@ApiModel
public class StoreTechnicianRelateListRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号码",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "证件类型",name = "certificateType")
    private String certificateType;

    @ApiModelProperty(value = "证件号码",name = "certificateNum")
    private String certificateNum;

    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;
}
