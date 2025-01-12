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
@ApiModel(value = "InDoorSceneOrderReq", description = "上门现场订单VO")
public class SceneOrderDescribeReq {

    @ApiModelProperty(value = "现场订单uuid", name = "sceneOrderUuid")
    @NotBlank(message = "请输入现场订单uuid")
    private String sceneOrderUuid;

    @ApiModelProperty(value = "描述", name = "describe")
    @NotBlank(message = "描述")
    private String describe;

    @ApiModelProperty(value = "图片", name = "imageList")
    private List<String> imageList;

    @ApiModelProperty(value = "类型1上门，2完成维修", name = "type")
    private Integer type;


}
