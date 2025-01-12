package com.car.account.client.request.addr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangyp
 * @date 2021/1/14 22:53
 */
@Data
@ApiModel
public class ReceiveAddrReq {

    /**
     * 省份
     */
    @ApiModelProperty(value="省份uuid",name="province",example = "上海市uuid")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value="市uuid",name="city",example = "上海市uuid")
    private String city;

    @ApiModelProperty(value="区uuid",name="area",example = "静安区uuid")
    private String area;
    /**
     * 标记家/公司/其他
     */
    @ApiModelProperty(value="标记家/公司/其他",name="sign")
    private String sign;

    /**
     * 联系人
     */
    @NotEmpty
    @Length(max = 32)
    @ApiModelProperty(value="联系人",name="contactor")
    private String contactor;

    /**
     * 联系电话
     */
    @NotEmpty
    @Length(max = 11)
    @ApiModelProperty(value="联系电话",name="phone")
    private String phone;

    /**
     * 是否默认地址0是1否
     */
    @ApiModelProperty(value="是否默认地址0是1否",name="defaultFlag")
    private Integer defaultFlag;

    /**
     * 详细地址
     */
    @NotEmpty
    @Length(max = 256)
    @ApiModelProperty(value="详细地址",name="addr")
    private String addr;
}
