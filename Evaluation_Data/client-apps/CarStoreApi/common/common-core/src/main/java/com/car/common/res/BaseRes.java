package com.car.common.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName BaseRes
 * @Version 1.0
 */
@Data
public class BaseRes {

    /**
     * 是否有效 0:生效 1：无效
     */
    @ApiModelProperty(value = "是否有效 0:生效 1：无效 ")
    private Integer sts;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createdBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String lastUpdatedBy;
}
