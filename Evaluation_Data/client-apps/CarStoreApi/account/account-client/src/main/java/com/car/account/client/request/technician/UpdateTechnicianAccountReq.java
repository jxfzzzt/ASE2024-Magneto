package com.car.account.client.request.technician;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/23
 */
@Data
@ApiModel(value = "UpdateTechnicianAccountReq", description = "修改技师账户信息VO")
public class UpdateTechnicianAccountReq {

    @ApiModelProperty(value = "技师uuid", name = "technicianUuid")
    @NotBlank(message = "请输入技师uuid")
    private String technicianUuid;

    @ApiModelProperty(value = "订单金额", name = "orderAmount")
    @NotNull(message = "请输入订单金额")
    private BigDecimal orderAmount;
}
