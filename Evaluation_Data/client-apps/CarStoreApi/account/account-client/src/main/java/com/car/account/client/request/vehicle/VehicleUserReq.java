package com.car.account.client.request.vehicle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/12/19 18:31
 */
@Data
@ApiModel
public class VehicleUserReq {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "姓名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "证件类型",name = "certificateType")
    private String certificateType;

    @ApiModelProperty(value = "证件号码",name = "certificateNum")
    private String certificateNum;

    @ApiModelProperty(value = "联系方式",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "地址-省",name = "addressProvince")
    private String addressProvince;

    @ApiModelProperty(value = "地址-市",name = "addressCity")
    private String addressCity;

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
    private List<VehicleReq> vehicleList;

    @ApiModelProperty(value = "头像链接地址",name = "photoImgUrl")
    private String photoImgUrl;

}
