package com.car.account.client.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Data
@ApiModel
public class CheckStoreTechnicianRelateReq {

    @NotBlank(message = "请输入主键uuid")
    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @NotNull(message = "请填写审核状态!")
    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;
}
