package com.car.account.client.request.withdrawal;

import com.car.common.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/26
 */
@ApiModel(value="QueryWithdrawalListReq",description="查询提现列表VO对象")
@Data
public class QueryWithdrawalListReq extends PageReq {

    @ApiModelProperty(value = "姓名",name = "userName")
    private String userName;

    @ApiModelProperty(value = "手机号",name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "查询当前用户(店铺、技师)提现列表",name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value="提现发起时间 yyyy-MM-dd HH:mm:ss",name="startTime", example = "2020-12-26 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty(value = "用户角色",name = "userRole")
    private String userRole;

    @ApiModelProperty(value = "审核状态 (0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

}
