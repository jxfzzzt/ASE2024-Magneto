package com.car.account.client.response.goods;

import com.car.account.client.response.goods.sub.GoodsDetailRes;
import com.car.account.client.response.goods.sub.GoodsImgRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author zhangyp
 * @date 2021/1/16 16:31
 */
@Data
@ApiModel
public class GoodsRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    @ApiModelProperty(value = "店铺名称",name = "storeName")
    private String storeName;

    @ApiModelProperty(value = "店铺主键",name = "storeName")
    private String storeUuid;

    @ApiModelProperty(value = "工时费",name = "manHourCost")
    private BigDecimal manHourCost;

    @ApiModelProperty(value = "材料费",name = "materialsExpenses")
    private BigDecimal materialsExpenses;

    @ApiModelProperty(value = "库存",name = "surplusNum")
    private Integer surplusNum;

    @ApiModelProperty(value = "销量",name = "salesNum")
    private Integer salesNum;

    @ApiModelProperty(value = "销售状态:0 库存 1 在售",name = "sellSts")
    private Integer sellSts;

    @ApiModelProperty(value = "创建时间",name = "createdTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date createdTime;

    @ApiModelProperty(value = "图片列表",name = "imgList")
    private List<GoodsImgRes> imgList;

    @ApiModelProperty(value = "物料列表",name = "detailList")
    private List<GoodsDetailRes> detailList;

    @ApiModelProperty(value = "商品金额",name = "amt")
    private BigDecimal amt;

    @ApiModelProperty(value = "商品描述",name = "goodsDescribe")
    private String goodsDescribe;

    @ApiModelProperty(value = "一级分类",name = "levelOne")
    private String levelOne;
    @ApiModelProperty(value = "一级分类uuid",name = "levelOneUuid")
    private String levelOneUuid;
    @ApiModelProperty(value = "二级分类",name = "levelTwo")
    private String levelTwo;
    @ApiModelProperty(value = "二级分类uuid",name = "levelTwoUuid")
    private String levelTwoUuid;
    @ApiModelProperty(value = "商品类型",name = "goodsType")
    private String goodsType;
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;

    @ApiModelProperty(value = "商品评分",name = "score")
    private Float score;
    @ApiModelProperty(value = "商品评论数量",name = "commentNum")
    private Integer commentNum;

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
