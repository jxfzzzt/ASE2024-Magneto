package com.car.order.client.request.order.goods;

import com.car.order.client.request.order.goods.sub.AddOrderGoodsDetailReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 下单
 * @author zhouz
 * @date 2020/12/30
 */
@Data
@ApiModel(value="AddOrderGoodsReq",description="新增商品订单请求VO")
public class AddOrderGoodsReq {

    @NotBlank(message = "商品唯一标识必填！")
    @ApiModelProperty(value = "商品唯一标识",name = "goodsUuid")
    private String goodsUuid;

    @NotBlank(message = "请输入服务地区！")
    @ApiModelProperty(value = "服务地区",name = "serviceArea")
    private String serviceArea;

    @NotBlank(message = "请输入联系人！")
    @ApiModelProperty(value = "联系人",name = "contacts")
    private String contacts;

    @NotBlank(message = "请输入手机号！")
    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @NotBlank(message = "请输入支付方式！")
    @ApiModelProperty(value = "支付方式",name = "payType")
    private String payType;

    @NotBlank(message = "请输入配送方式！")
    @ApiModelProperty(value = "配送方式",name = "deliveryMode")
    private String deliveryMode;

    @NotBlank(message = "请输入配送地址！")
    @ApiModelProperty(value = "配送地址",name = "deliveryAddress")
    private String deliveryAddress;

    @ApiModelProperty(value = "订单备注信息",name = "orderRemark")
    private String orderRemark;

    @NotEmpty(message = "请选择商品！")
    @ApiModelProperty(value = "商品列表",name = "orderGoodsDetailListReq")
    private List<AddOrderGoodsDetailReq> orderGoodsDetailListReq;


    @ApiModelProperty(value = "快递费用",name = "amtExpress")
    private BigDecimal amtExpress;

    @ApiModelProperty(value = "服务费",name = "amtService")
    private BigDecimal amtService;


}
