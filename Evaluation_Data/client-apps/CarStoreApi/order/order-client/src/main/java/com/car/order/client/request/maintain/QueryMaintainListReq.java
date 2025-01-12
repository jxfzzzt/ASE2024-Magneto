package com.car.order.client.request.maintain;

import com.car.common.req.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;


import javax.validation.constraints.NotBlank;

/**
 * @program: car-service
 * @description:
 * @author: niushuaixiang
 * @create: 2021-03-18 18:52
 */
@Data
@ApiModel(value="QueryMaintainListReq",description="查询养护请求VO")
public class QueryMaintainListReq extends PageReq {


    /**
     * 养护标题
     */
    @ApiModelProperty(value = "养护标题", name = "maintainTitle")
    private String maintainTitle;



    /**
     * 品牌uuid
     */
    @ApiModelProperty(value = "品牌uuid", name = "brandUuid")
    private String brandUuid;

    /**
     * 车型uuid
     */
    @ApiModelProperty(value = "车型", name = "carModelUuid")
    private String carModelUuid;

    /**
     * 所属系统字典表attach_sys
     */
    @ApiModelProperty(value = "所属系统", name = "attachSys")
    private String attachSys;


    /**
     * 审核状态:0 待审核 1 审核通过 2 审核驳回
     */
    @ApiModelProperty(value = "审核状态", name = "maintainCheckSts")
    private Integer maintainCheckSts;


}