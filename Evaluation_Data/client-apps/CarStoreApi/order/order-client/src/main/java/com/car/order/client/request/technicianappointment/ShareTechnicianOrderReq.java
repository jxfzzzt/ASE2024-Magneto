package com.car.order.client.request.technicianappointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预约技师订单入参
 *
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.client.request.technicianappointment
 * @NAME: ShareTechnicianOrderReq
 * @DATE: 2021/3/4 21:22
 */
@Data
@ApiModel(value = "ShareTechnicianOrderReq", description = "预约技师订单入参")
public class ShareTechnicianOrderReq {

    /**
     * 技师UUID
     */
    @ApiModelProperty(value = "技师UUID")
    @NotBlank(message = "未选择维修技师")
    private String technicianUuid;

    /**
     * 预约地点
     */
    @ApiModelProperty(value = "预约地点")
    @NotBlank(message = "请输入预约地点")
    private String appointmentAddress;

    /**
     * 预约时间
     */
    @ApiModelProperty(value = "预约时间")
    @NotNull(message = "请选择预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date appointmentTime;

    /**
     * 品牌UUID
     */
    @ApiModelProperty(value = "品牌UUID")
    @NotBlank(message = "请选择车型品牌")
    private String brandUuid;

    /**
     * 车型UUID
     */
    @ApiModelProperty(value = "车型UUID")
    @NotBlank(message = "请选择车型信息")
    private String modelUuid;

    /**
     * 故障描述
     */
    @ApiModelProperty(value = "故障描述")
    @NotBlank(message = "故障描述不能为空")
    private String faultDescription;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal payNum;
    /**
     * 技师预约费用ID
     */
    @ApiModelProperty(value = "技师预约费用ID")
    private String reservationMoneyUuid;

}
