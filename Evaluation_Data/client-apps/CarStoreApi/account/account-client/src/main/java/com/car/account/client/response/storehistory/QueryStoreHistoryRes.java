package com.car.account.client.response.storehistory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class QueryStoreHistoryRes {

    @ApiModelProperty(value = "记录ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺ID",name = "storeUuid")
    private String storeUuid;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;



}
