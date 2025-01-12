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
public class AddStoreTechnicianRelateReq extends AddRelateStoreReq{

    /**
     * 技师uuid
     */
    @NotBlank(message = "请输入技师uuid！")
    @ApiModelProperty(value = "技师uuid",name = "technicianUuid")
    private String technicianUuid;

}
