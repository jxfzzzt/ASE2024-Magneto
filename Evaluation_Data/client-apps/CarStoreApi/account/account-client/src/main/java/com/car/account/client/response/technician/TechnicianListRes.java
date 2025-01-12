package com.car.account.client.response.technician;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianListRes {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号码",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "省份",name = "addressProvince")
    private String addressProvince;

    @ApiModelProperty(value = "省份名字",name = "addressProvinceName")
    private String addressProvinceName;

    @ApiModelProperty(value = "城市",name = "addressCity")
    private String addressCity;

    @ApiModelProperty(value = "城市名字",name = "addressCityName")
    private String addressCityName;

    @ApiModelProperty(value = "详细地址",name = "addressDetail")
    private String addressDetail;

    @ApiModelProperty(value = "技术类型",name = "technologyType")
    private String technologyType;

    @ApiModelProperty(value = "技术类型名字",name = "technologyTypeName")
    private String technologyTypeName;

    @ApiModelProperty(value = "维修品牌",name = "technicianBrandName")
    private String technicianBrandName;

    @ApiModelProperty(value = "订单总数",name = "orderCount")
    private Integer orderCount;

    @ApiModelProperty(value = "案例总数",name = "caseCount")
    private Integer caseCount;

    @ApiModelProperty(value = "问答总数",name = "qaCount")
    private Integer qaCount;

    @ApiModelProperty(value = "现场支持总数",name = "supportCount")
    private Integer supportCount;

    @ApiModelProperty(value = "评分",name = "score")
    private BigDecimal score;

    @ApiModelProperty(value = "审核状态(0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "工龄", name = "workingYear")
    private Integer workingYear;

    @ApiModelProperty(value = "国家等级鉴定", name = "stateVerification")
    private String stateVerification;

    @ApiModelProperty(value = "主机厂认证", name = "hostAuthentication")
    private String hostAuthentication;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

}
