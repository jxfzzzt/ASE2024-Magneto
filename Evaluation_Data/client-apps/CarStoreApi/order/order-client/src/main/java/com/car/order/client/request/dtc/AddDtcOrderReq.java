package com.car.order.client.request.dtc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/18
 */
@Data
@ApiModel(value="AddDtcOrderReq",description="DTC新增故障订单请求VO")
public class AddDtcOrderReq {

    @ApiModelProperty(value = "dtc故障uuid", name = "dtcUuid")
    @NotBlank(message = "请输入dtc故障uuid！")
    private String dtcUuid;

}
