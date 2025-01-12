package com.car.order.client.response.technicianappointment;

import com.car.common.res.BaseRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 预约技师订单出参
 *
 * @author zhoujian
 * @PACKAGE_NAME: com.car.order.client.request.technicianappointment
 * @NAME: ShareTechnicianOrderRes
 * @DATE: 2021/3/4 21:22
 */
@Data
@ApiModel(value = "ShareTechnicianOrderRes", description = "预约技师订单出参")
public class ShareTechnicianOrderRes extends BaseRes {

    @ApiModelProperty(value = "数据记录UUID")
    private String uuid;

    @ApiModelProperty(value = "订单号")
    private String orderNum;

    /**
     * 车主UUID
     */
    @ApiModelProperty(value = "车主UUID")
    private String ownerUuid;

    /**
     * 技师UUID
     */
    @ApiModelProperty(value = "技师UUID")
    private String technicianUuid;

    /**
     * 预约地点
     */
    @ApiModelProperty(value = "预约地点")
    private String appointmentAddress;

    /**
     * 预约时间
     */
    @ApiModelProperty(value = "预约时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date appointmentTime;

    /**
     * 品牌UUID
     */
    @ApiModelProperty(value = "品牌UUID")
    private String brandUuid;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 车型UUID
     */
    @ApiModelProperty(value = "车型UUID")
    private String modelUuid;

    /**
     * 车型名称
     */
    @ApiModelProperty(value = "车型名称")
    private String modelName;

    /**
     * 故障描述
     */
    @ApiModelProperty(value = "故障描述")
    private String faultDescription;

    /**
     * 订单预约状态
     * 1：待付款
     * 2：待接单
     * 3：待服务
     * 4：已完成
     * 5：退款中
     * 6：已退款
     */
    @ApiModelProperty(value = "订单预约状态 1：待付款 2：待服务 3：已完成 4：退款中 5：已退款 6：待接单")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单预约状态名称")
    private String orderStatusName;

    @ApiModelProperty(value = "支付方式 0：微信 1：支付宝")
    private Integer payType;

    /**
     * 订单金额
     */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal payNum;

    /**
     * 平台服务费用
     */
    @ApiModelProperty(value = "平台服务费用")
    private BigDecimal platformMoney;

    /**
     * 技师预约费用
     */
    @ApiModelProperty(value = "技师预约费用")
    private BigDecimal reservationMoney;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    private String orderDesc;

}
