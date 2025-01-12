package com.car.account.client.response.vehicle.vehicleUser;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class VehicleUserRes {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户类型 1：注册用户  2：车主",name = "userType")
    private Integer userType;

    @ApiModelProperty(value = "姓名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "证件类型",name = "certificateType")
    private String certificateType;

    @ApiModelProperty(value = "证件类型名称",name = "certificateTypeName")
    private String certificateTypeName;

    @ApiModelProperty(value = "证件号码",name = "certificateNum")
    private String certificateNum;

    @ApiModelProperty(value = "联系方式",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "地址-省",name = "addressProvince")
    private String addressProvince;

    @ApiModelProperty(value = "省份名称",name = "addressProvinceName")
    private String addressProvinceName;

    @ApiModelProperty(value = "地址-市",name = "addressCity")
    private String addressCity;

    @ApiModelProperty(value = "城市名称",name = "addressCityName")
    private String addressCityName;

    @ApiModelProperty(value = "详细地址",name = "addressDetail")
    private String addressDetail;

    @ApiModelProperty(value = "开户名",name = "accountName")
    private String accountName;

    @ApiModelProperty(value = "开户银行",name = "depositBank")
    private String depositBank;

    @ApiModelProperty(value = "支行名称",name = "subBranchName")
    private String subBranchName;

    @ApiModelProperty(value = "银行卡号",name = "cardNumbers")
    private String cardNumbers;

    @ApiModelProperty(value = "支付宝账号",name = "alipayAccount")
    private String alipayAccount;

    @ApiModelProperty(value = "用户车辆信息",name = "vehicleList")
    private List<VehicleRes> vehicleList;

    @ApiModelProperty(value = "头像图片链接地址",name = "photoImgUrl")
    private String photoImgUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间",name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "纬度",name = "latitude")
    private Float latitude;

    @ApiModelProperty(value = "经度",name = "longitude")
    private Float longitude;

}
