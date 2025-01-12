package com.car.order.client.response.order.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@ApiModel(value="OrderGoodsRes",description="订单商品VO")
public class OrderGoodsRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺uuid",name = "storeUuid")
    private String storeUuid;

    @ApiModelProperty(value = "订单号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "下单时间 yyyy-MM-dd",name="createdTime",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdTime;

    @ApiModelProperty(value = "服务地区,保存具体地址",name = "serviceArea")
    private String serviceArea;

    @ApiModelProperty(value = "服务单号",name = "serviceNum")
    private String serviceNum;

    @ApiModelProperty(value = "实收金额",name = "actualAmount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "应收金额",name = "receivableAmount")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "联系人",name = "contacts")
    private String contacts;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "支付方式",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "配送方式",name = "deliveryMode")
    private Integer deliveryMode;

    @ApiModelProperty(value = "配送地址",name = "deliveryAddress")
    private String deliveryAddress;

    @ApiModelProperty(value = "订单备注信息",name = "orderRemark")
    private String orderRemark;

    @ApiModelProperty(value = "退款类型: 0 线上退款 1 线下退款",name = "refundType")
    private Integer refundType;

    @ApiModelProperty(value = "退款金额",name = "refundAmount")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "售后原因",name = "afterSaleCause")
    private String afterSaleCause;

    @ApiModelProperty(value = "售后状态:0 等待买家退货 1 已退货 待收货 2 已收货 换货中 3 系统退款中 4 已完成 5 已取消",name = "afterSaleSts")
    private Integer afterSaleSts;

    @ApiModelProperty(value = "退款状态:0 同意退款 1 拒绝退款 2 取消退款",name = "refundSts")
    private Integer refundSts;

    @ApiModelProperty(value = "售后说明",name = "afterSaleRemark")
    private String afterSaleRemark;

    @ApiModelProperty(value = "评价状态: 0 未评论  1 已评论 2 好评 3 中评 4 差评",name = "evaluateSts")
    private Integer evaluateSts;

    @ApiModelProperty(value = "店铺评分",name = "storeScore")
    private BigDecimal storeScore;

    @ApiModelProperty(value = "技师评分",name = "technicianScore")
    private BigDecimal technicianScore;

    @ApiModelProperty(value = "商品列表",name = "orderGoodsDetailListRes")
    private List<OrderGoodsDetailRes> orderGoodsDetailListRes;
}
