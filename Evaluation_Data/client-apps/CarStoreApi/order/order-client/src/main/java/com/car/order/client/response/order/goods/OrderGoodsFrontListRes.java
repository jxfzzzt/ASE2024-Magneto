package com.car.order.client.response.order.goods;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhouz
 * @date 2020/12/30
 */
@ApiModel(value="OrderGoodsFrontListRes",description="查询维修保养订单VO对象")
@Data
public class OrderGoodsFrontListRes extends PageReq {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "实收金额",name = "actualAmount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "商品数量",name = "goodsNum")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片地址",name = "goodsImgUrl")
    private String goodsImgUrl;

    @ApiModelProperty(value = "配送方式0快递1自取",name = "deliveryMode")
    private Integer deliveryMode;

    @ApiModelProperty(value = "商品价格", name = "goodsPrice")
    private BigDecimal goodsPrice;

    @ApiModelProperty(value = "物流费用", name = "amtExpress")
    private BigDecimal amtExpress;

    @ApiModelProperty(value = "服务费用", name = "amtService")
    private BigDecimal amtService;

    @ApiModelProperty(value = "联系人", name = "contacts")
    private String contacts;

    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "订单类型 0 线上直接发货，无需服务， 1 购买商品且需要服务， 2 无商品，仅线下服务")
    private Integer orderType;

}
