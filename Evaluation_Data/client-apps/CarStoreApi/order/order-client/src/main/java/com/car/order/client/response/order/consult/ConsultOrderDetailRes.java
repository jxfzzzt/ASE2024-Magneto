package com.car.order.client.response.order.consult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Data
@ApiModel(value="ConsultOrderDetailRes",description="咨询订单详情信息VO")
public class ConsultOrderDetailRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单编号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "订单金额",name = "orderAmount")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "应收金额",name = "receivableAmount")
    private BigDecimal receivableAmount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "下单时间",name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "采纳结果 0 满意 1 不满意",name = "evaluateSts")
    private Integer acceptResult;

    @ApiModelProperty(value = "车主uuid",name = "carOwnerUuid")
    private String carOwnerUuid;

    @ApiModelProperty(value = "车主姓名",name = "carOwnerName")
    private String carOwnerName;

    @ApiModelProperty(value = "车主手机号",name = "carOwnerMobile")
    private String carOwnerMobile;

    @ApiModelProperty(value = "车主头像",name = "carOwnerImgUrl")
    private String carOwnerImgUrl;

    @ApiModelProperty(value = "技师评分",name = "technicianScore")
    private BigDecimal technicianScore;

    @ApiModelProperty(value = "评价状态: 0 未评论  1 已评论",name = "evaluateTime")
    private Integer evaluateSts;

    @ApiModelProperty(value = "评价时间",name = "evaluateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date evaluateTime;

    @ApiModelProperty(value = "咨询内容与答复信息",name = "consultRes")
    private ConsultRes consultRes;

    @ApiModelProperty(value = "车辆品牌",name = "vehicleBrand")
    private String vehicleBrand;

    @ApiModelProperty(value = "车型类型",name = "vehicleModel")
    private String vehicleModel;
}
