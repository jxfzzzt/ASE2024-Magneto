package com.car.account.client.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value="StoreUserReq",description="店铺联系人请求VO")
public class StoreUserReq {

    @ApiModelProperty(value = "店铺联系人uuid")
    private String uuid;

    @NotBlank(message = "请输入联系人姓名！")
    @ApiModelProperty(value = "联系人姓名",name = "userName")
    private String userName;

    @NotBlank(message = "请输入手机号！")
    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @NotBlank(message = "请选择人员类型！")
    @ApiModelProperty(value = "人员类型 对应字典表 uuid", name = "personType")
    protected String personType;

    @ApiModelProperty(value = "邮箱",name = "email")
    private String email;

    @ApiModelProperty(value = "证件类型1身份证2护照",name = "idType")
    private Integer idType;

    @ApiModelProperty(value = "证件号码",name = "idNum")
    private String idNum;

    @ApiModelProperty(value = "是否是子账户",name = "isSubAccount")
    private Byte isSubAccount;

}
