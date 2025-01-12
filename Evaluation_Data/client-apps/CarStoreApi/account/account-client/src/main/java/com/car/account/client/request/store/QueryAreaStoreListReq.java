package com.car.account.client.request.store;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhouz
 * @date 2021/1/24
 */
@ApiModel
@Data
public class QueryAreaStoreListReq extends PageReq {

    @NotBlank(message = "请输入区域id")
    @ApiModelProperty(value = "区域Uuid",name = "areaUuid")
    private String areaUuid;

}
