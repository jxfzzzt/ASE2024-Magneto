package com.car.account.client.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@Data
@ApiModel
public class AddRelateStoreReq {

    @NotBlank(message = "请输入店铺uuid！")
    @ApiModelProperty(value = "店铺uuid",name = "storeUuid")
    private String storeUuid;

}
