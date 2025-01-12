package com.car.account.client.request.store;

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
 * Date:  2021/2/24
 */
@Data
@ApiModel(value = "UpdateStoreAccountReq", description = "修改店铺账户信息VO")
public class UpdateStoreAccountReq {

    @ApiModelProperty(value = "店铺uuid", name = "technicianUuid")
    @NotBlank(message = "请输入店铺uuid")
    private String storeUuid;

    @ApiModelProperty(value = "订单金额", name = "orderAmount")
    @NotNull(message = "请输入订单金额")
    private BigDecimal orderAmount;
}
