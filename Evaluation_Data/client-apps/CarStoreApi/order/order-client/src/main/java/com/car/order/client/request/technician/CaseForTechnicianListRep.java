package com.car.order.client.request.technician;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2021/1/23
 */
@ApiModel
@Data
public class CaseForTechnicianListRep extends PageReq {

    @ApiModelProperty(value="技师uuid",name="technicianUuid")
    private String technicianUuid;
}
