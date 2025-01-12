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
@ApiModel(value="OrderGoodsFrontRes",description="订单商品VO")
public class OrderGoodsFrontRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "下单时间 yyyy-MM-dd",name="createdTime",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdTime;

    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "商品数量",name = "goodsNum")
    private Integer goodsNum;

    @ApiModelProperty(value = "商品图片地址",name = "goodsImgUrl")
    private String goodsImgUrl;

    @ApiModelProperty(value = "店铺uuid",name = "storeUuid")
    private String storeUuid;

    @ApiModelProperty(value = "店铺名称", name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "公司名称", name = "companyName")
    private String companyName;

    @ApiModelProperty(value = "公司地址-详细信息", name = "companyAddressDetail")
    private String companyAddressDetail;

    @ApiModelProperty(value = "配送地址", name = "deliveryAddress")
    private String deliveryAddress;

    @ApiModelProperty(value = "收件人", name = "contacts")
    private String contacts;

    @ApiModelProperty(value = "联系方式", name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "实收金额",name = "actualAmount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "商品价格",name = "receivableAmount")
    private BigDecimal receivableAmount;

    @ApiModelProperty(value = "支付方式",name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "配送方式 0快递1自取",name = "deliveryMode")
    private Integer deliveryMode;

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

    @ApiModelProperty(value = "商品详情列表",name = "orderGoodsDetailFrontRes")
    private List<OrderGoodsDetailFrontRes> orderGoodsDetailFrontRes;


    @ApiModelProperty(value = "物流费用", name = "amtExpress")
    private BigDecimal amtExpress;

    @ApiModelProperty(value = "服务费用", name = "amtService")
    private BigDecimal amtService;


    @ApiModelProperty(value = "订单类型 0 线上直接发货，无需服务， 1 购买商品且需要服务， 2 无商品，仅线下服务")
    private Integer orderType;

    @ApiModelProperty(value = "订单状态：0未完成 1已完成")
    private Integer serviceSts;

    @ApiModelProperty(value = "商品评分", name = "goodsScore")
    private BigDecimal goodsScore;

    @ApiModelProperty(value = "服务评分", name = "serviceScore")
    private BigDecimal serviceScore;
}
