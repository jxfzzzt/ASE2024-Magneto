package com.car.account.client.request.goods;

import com.car.account.client.request.goods.sub.GoodsDetailReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Data
@ApiModel(value="UpdateGoodsReq",description="修改商品请求VO")
public class UpdateGoodsReq {

    @NotBlank(message = "[UpdateGoodsReq] uuid is required！")
    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "店铺uuid",name = "storeUuid")
    private String storeUuid;

    @ApiModelProperty(value = "商品类型",name = "goodsType")
    private String goodsType;

    @ApiModelProperty(value = "父类型",name = "parentType")
    private String parentType;

    @ApiModelProperty(value = "工时费",name = "manHourCost")
    private BigDecimal manHourCost;

    @ApiModelProperty(value = "材料费",name = "materialsExpenses")
    private BigDecimal materialsExpenses;

    @ApiModelProperty(value = "库存",name = "surplusNum")
    private Integer surplusNum;

    @ApiModelProperty(value = "商品图片",name = "goodsImgListReq")
    private List<GoodsImgReq> goodsImgListReq;

    @ApiModelProperty(value = "销售状态:0 库存 1 在售",name = "sellSts")
    @Max(value = 1, message = "销售状态数值不能大于1")
    @Min(value = 0, message = "销售状态数值不能小于0")
    private Integer sellSts;

    @ApiModelProperty(value = "描述",name = "goodsDescribe")
    private String goodsDescribe;

    List<GoodsDetailReq> goodsDetailReqList;

    @ApiModelProperty(value = "平台服务费",name = "platformServiceMoney")
    private BigDecimal platformServiceMoney;

    @ApiModelProperty(value = "车辆品牌",name = "vehicleBrand")
    private String vehicleBrand;

    @ApiModelProperty(value = "车型类型",name = "vehicleModel")
    private String vehicleModel;

    @ApiModelProperty(value = "轮胎编号",name = "tyreNo")
    private String tyreNo;

    @ApiModelProperty(value = "配送方式",name = "receiveMethod")
    private Integer receiveMethod;
}
