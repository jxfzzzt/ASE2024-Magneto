package com.car.order.client.request.dtc;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/17
 */
@Data
@ApiModel(value = "QueryDtcListReq", description = "查询dtc故障列表请求信息，接收参数VO")
public class QueryDtcListReq extends PageReq {

    @ApiModelProperty(value = "dtc故障代码", name = "dtcCode")
    private String dtcCode;


    @ApiModelProperty(value = "审核状态:0 待审核 1 审核通过 2 审核驳回",name = "dtcCheckSts")
    private Integer dtcCheckSts;
}
