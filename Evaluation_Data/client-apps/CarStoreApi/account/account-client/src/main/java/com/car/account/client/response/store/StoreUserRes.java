package com.car.account.client.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/19
 */
@Data
@ApiModel(value="StoreUserRes",description="店铺联系人请求VO")
public class StoreUserRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "联系人姓名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "人员类型对应字典表 uuid",name = "personType")
    private String personType;

    @ApiModelProperty(value = "邮箱",name = "email")
    private String email;

    @ApiModelProperty(value = "店铺uuid", name = "storeUuid")
    private String storeUuid;

}
