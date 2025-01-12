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
 * Date:  2021/2/27
 */
@Data
@ApiModel(value = "SceneOrderConfirmReq", description = "确认现场订单VO")
public class SceneOrderConfirmReq {

    @ApiModelProperty(value = "现场订单uuid", name = "sceneOrderUuid")
    @NotBlank(message = "请输入现场订单uuid")
    private String sceneOrderUuid;

    @ApiModelProperty(value = "类型1上门，2完成维修", name = "type")
    private Integer type;


}
