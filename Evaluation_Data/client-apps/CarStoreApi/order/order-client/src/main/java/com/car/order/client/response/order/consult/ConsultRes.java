package com.car.order.client.response.order.consult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author zhouz
 * @date 2021/1/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ConsultRes",description="咨询内容信息VO")
public class ConsultRes extends ConsultBaseRes{

    @ApiModelProperty(value = "技师uuid",name = "technicianUuid")
    private String technicianUuid;

    @ApiModelProperty(value = "技师手机号",name = "technicianMobile")
    private String technicianMobile;

    @ApiModelProperty(value = "车主uuid",name = "carOwnerUuid")
    private String carOwnerUuid;

    @ApiModelProperty(value = "车主手机号",name = "carOwnerMobile")
    private String carOwnerMobile;

    @ApiModelProperty(value = "车主头像",name = "carOwnerImgUrl")
    private String carOwnerImgUrl;

    @ApiModelProperty(value = "答复状态 0 未答复 1 已答复",name = "answerSts")
    private Integer answerSts;

    @ApiModelProperty(value = "答复时间",name = "answerTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date answerTime;

    @ApiModelProperty(value = "支咨询审核状态 0 待审核 1 审核通过 2 审核驳回",name = "consultCheckSts")
    private Integer consultCheckSts;

    @ApiModelProperty(value = "回答审核状态 0 待审核 1 审核通过 2 审核驳回",name = "answerCheckSts")
    private Integer answerCheckSts;

    @ApiModelProperty(value = "咨询图片列表",name = "consultImgList")
    private List<String> consultImgList;

    @ApiModelProperty(value = "回答图片列表",name = "answerImgList")
    private List<String> answerImgList;

    @ApiModelProperty(value = "订单状态 0 待支付 1 已支付 2: 已取消 3:退款中 4:退款成功 5:退款失败",name = "orderSts")
    private Integer orderSts;

    @ApiModelProperty(value = "咨询类型  1：技师提问  2：全国技师提问",name = "consultType")
    private Integer consultType;

    @ApiModelProperty(value = "车辆品牌",name = "vehicleBrand")
    private String vehicleBrand;

    @ApiModelProperty(value = "车型类型",name = "vehicleModel")
    private String vehicleModel;
}
