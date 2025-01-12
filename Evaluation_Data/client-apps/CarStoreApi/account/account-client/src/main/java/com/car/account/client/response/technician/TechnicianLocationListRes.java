package com.car.account.client.response.technician;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author linjiang.xie
 * @date 2020/12/19 15:45
 */
@Data
@ApiModel
public class TechnicianLocationListRes {
    @ApiModelProperty(value = "数据ID",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "工龄", name = "workingYear")
    private Integer workingYear;

    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;

   /**
     * 关联店铺
     */
    @ApiModelProperty(value = "关联店铺", name = "relationStoreUuid")
    private String relationStoreUuid;
    /**
     * 地址详情经度
     */
    @ApiModelProperty(value = "地址详情经度")
    private Float addressLongitude;

    /**
     * 地址详情纬度
     */
    @ApiModelProperty(value = "地址详情纬度")
    private Float addressLatitude;

}
