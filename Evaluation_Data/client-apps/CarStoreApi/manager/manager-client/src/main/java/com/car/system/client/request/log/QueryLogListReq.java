package com.car.system.client.request.log;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author linjiang.xie
 * @date 2019/6/3 11:02
 */
@Data
public class QueryLogListReq extends PageReq {

    @ApiModelProperty(value = "检索内容",name = "searchValue")
    private String searchValue;

    @ApiModelProperty(value = "开始时间",name = "beginTime")
    private String beginTime;

    @ApiModelProperty(value = "结束时间",name = "endTime")
    private String endTime;

}
