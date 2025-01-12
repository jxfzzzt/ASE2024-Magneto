package com.car.order.client.request.scene;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/27
 */
@Data
@ApiModel(value = "AddSceneOrderServiceReq", description = "新增现场下单服务订单VO")
public class AddSceneOrderServiceReq {


    @ApiModelProperty(value = "现场订单uuid", name = "sceneOrderUuid")
    @NotBlank(message = "请输入现场订单uuid")
    private String sceneOrderUuid;

    @ApiModelProperty(value = "故障描述", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "解决方案", name = "solution")
    private String solution;

    @ApiModelProperty(value = "基本检查费用Uuid", name = "basicInspectAmountUuid")
    @NotBlank(message = "请选择基本检查费用！")
    private String basicInspectAmountUuid;

    @ApiModelProperty(value = "相关线路检查费用Uuid", name = "lineInspectAmountUuid")
    private String lineInspectAmountUuid;

    @ApiModelProperty(value = "诊断仪使用费Uuid", name = "diagnosisInstrumentAmountUuid")
    private String diagnosisInstrumentAmountUuid;

    @ApiModelProperty(value = "相关线路检查费用Uuid", name = "carSheetMetalAmountUuid")
    private String carSheetMetalAmountUuid;

    @ApiModelProperty(value = "车辆油漆修复费用Uuid", name = "carPaintRepairAmountUuid")
    private String carPaintRepairAmountUuid;

    @ApiModelProperty(value = "其他费用费Uuid", name = "otherAmountUuid")
    private String otherAmountUuid;

    @ApiModelProperty(value = "平台订单服务费Uuid", name = "orderServiceAmountUuid")
    private String orderServiceAmountUuid;

    @ApiModelProperty(value = "总费用", name = "totalAmount")
    private BigDecimal totalAmount;


}
