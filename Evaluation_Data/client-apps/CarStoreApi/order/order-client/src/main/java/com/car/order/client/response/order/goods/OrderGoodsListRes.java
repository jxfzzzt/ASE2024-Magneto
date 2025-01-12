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
@ApiModel(value="OrderGoodsListRes",description="查询商品订单列表VO对象")
@Data
public class OrderGoodsListRes extends PageReq {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "订单号",name = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "下单时间 yyyy-MM-dd",name="createdTime",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdTime;

    @ApiModelProperty(value = "服务地区",name = "serviceArea")
    private String serviceArea;

    @ApiModelProperty(value = "服务单号",name = "serviceNum")
    private String serviceNum;

    @ApiModelProperty(value = "实收金额",name = "actualAmount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "联系人",name = "contacts")
    private String contacts;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

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

    @ApiModelProperty(value = "工时费",name = "manHourCost")
    private BigDecimal manHourCost;

    @ApiModelProperty(value = "材料费",name = "materialsExpenses")
    private BigDecimal materialsExpenses;
}
