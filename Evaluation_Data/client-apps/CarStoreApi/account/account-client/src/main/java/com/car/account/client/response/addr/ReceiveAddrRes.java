package com.car.account.client.response.addr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangyp
 * @date 2021/1/14 22:55
 */
@Data
@ApiModel
public class ReceiveAddrRes {

    /**
     * 用户主键
     */
    @ApiModelProperty(value="地址主键",name="uuid")
    private String uuid;

    /**
     * 省份
     */
    @ApiModelProperty(value="省份编码",name="province")
    private String province;

    @ApiModelProperty(value="省份中文",name="provinceName")
    private String provinceName;
    /**
     * 市
     */
    @ApiModelProperty(value="市编码",name="city")
    private String city;

    @ApiModelProperty(value="市中文",name="cityName")
    private String cityName;

    @ApiModelProperty(value="区编码",name="area")
    private String area;
    @ApiModelProperty(value="区中文",name="areaName")
    private String areaName;
    /**
     * 标记家/公司/其他
     */
    @ApiModelProperty(value="标记家/公司/其他",name="sign")
    private String sign;

    /**
     * 联系人
     */
    @ApiModelProperty(value="联系人",name="contactor")
    private String contactor;

    /**
     * 联系电话
     */
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
    @ApiModelProperty(value="详细地址",name="addr")
    private String addr;

}
