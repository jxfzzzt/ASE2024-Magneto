package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2021/1/26
 */
@Data
@ApiModel
public class StoreTechnicianRelateDetailRes {

    @ApiModelProperty(value = "技师uuid",name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "技师关联店铺信息",name = "storeRelateDetailRes")
    private StoreRelateDetailRes storeRelateDetailRes;
}
