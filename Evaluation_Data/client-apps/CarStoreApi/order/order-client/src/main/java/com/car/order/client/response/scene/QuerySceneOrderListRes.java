package com.car.order.client.response.scene;

import com.car.common.res.PageRes;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/3/5
 */
@Data
@ApiModel(value = "QuerySceneOrderListRes", description = "查询现场订单列表返回VO")
public class QuerySceneOrderListRes{

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "发布者uuid", name = "issuerUuid")
    private String issuerUuid;

    @ApiModelProperty(value = "发布者姓名", name = "issuerName")
    private String issuerName;

    @ApiModelProperty(value = "发布者手机号码", name = "issuerMobile")
    private String issuerMobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "购买者姓名", name = "buyerName")
    private String buyerName;

    @ApiModelProperty(value = "购买者手机号码", name = "buyerMobile")
    private String buyerMobile;

    @ApiModelProperty(value = "品牌名称", name = "brandName")
    private String brandName;

    @ApiModelProperty(value = "车型名称", name = "carModelName")
    private String carModelName;

    @ApiModelProperty(value = "故障描述", name = "faultDesc")
    private String faultDesc;

    @ApiModelProperty(value = "支付方式 0 微信支付 1 支付宝支付", name = "payType")
    private Integer payType;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中  4:退款成功  5:退款失败 6：已完成", name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "总支付费用", name = "totalAmount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "抢单状态 0未抢，1已抢", name = "grabbingOrdersSts")
    private Integer grabbingOrdersSts;

    @ApiModelProperty(value = "是否是本人发布，true：是， false：否", name = "isOneself")
    private Boolean isOneself;

    @ApiModelProperty(value = "距离(km)", name = "distance")
    private Float distance;
}
