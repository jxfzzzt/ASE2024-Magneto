package com.car.order.web.dto.scene;

import com.car.order.web.model.scene.SceneOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/13
 */
@Data
public class SceneOrderDto extends SceneOrder {

    @ApiModelProperty(value = "现场下单技师信息uuid")
    private String sceneOrderTechnicianUuid;

    @ApiModelProperty(value = "技师信息故障描述", name = "faultDesc")
    private String technicianFaultDesc;

    @ApiModelProperty(value = "技师信息已检过程", name = "alreadyInspect")
    private String technicianAlreadyInspect;

    @ApiModelProperty(value = "技师信息DTC故障code", name = "dtcCode")
    private String technicianDtcCode;

    @ApiModelProperty(value = "技师信息维修总结", name = "repairSummary")
    private String repairSummary;

    @ApiModelProperty(value = "技师信息故障是否解决 0解决，1未解决", name = "faultSolve")
    private Integer faultSolve;
}
