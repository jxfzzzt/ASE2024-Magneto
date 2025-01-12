package com.car.order.client.request.technicianappointment;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.client.request.technicianappointment
 * @NAME: UpdateShareTechnicianOrderReq
 * @DATE: 2021/3/5 0:12
 */
@Data
@ApiModel(value = "UpdateShareTechnicianOrderReq", description = "更新预约订单入参")
public class UpdateShareTechnicianOrderReq {

    @ApiModelProperty(value = "需要更新的订单UUID")
    private String uuid;

    /**
     * 订单预约状态
     * 1：等待接单
     * 2：接单成功
     * 3：订单取消
     * 4：订单完成
     */
    @ApiModelProperty(value = "待付款(1)待服务(2)已完成(3)退款中(4)已退款(5)")
    @NotEmpty(message = "未上传更新订单状态")
    private Integer orderStatus;

}
