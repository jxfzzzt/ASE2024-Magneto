package com.car.order.client.request.scene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/6
 */
@Data
@ApiModel(value = "AddSceneTechnicianInfoReq", description = "")
public class AddSceneTechnicianInfoReq {

    @ApiModelProperty(value = "现场订单uuid", name = "sceneOrderUuid")
    @NotBlank(message = "请输入现场订单uuid")
    private String sceneOrderUuid;

    @ApiModelProperty(value = "现场下单技师信息uuid")
    private String sceneOrderTechnicianUuid;

    @ApiModelProperty(value = "故障描述", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "已检过程", name = "alreadyInspect")
    private String alreadyInspect;

    @ApiModelProperty(value = "DTC故障code", name = "dtcCode")
    private String dtcCode;

    @ApiModelProperty(value = "维修总结", name = "repairSummary")
    private String repairSummary;

    @ApiModelProperty(value = "故障是否解决 0解决，1未解决", name = "faultSolve")
    private Integer faultSolve;

    @ApiModelProperty(value = "Dtc故障图片", name = "dtcImgList")
    private List<String> dtcImgList;
}
