package com.car.account.client.request.withdrawal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhouz
 * @date 2020/12/27
 */
@Data
@ApiModel(value="UpdateWithdrawalReq",description="审核提现请求VO")
public class UpdateWithdrawalReq {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "转账凭证图片url",name = "voucherImgUrl")
    private String voucherImgUrl;

    @ApiModelProperty(value = "审核状态 (0:待审核 1:审核通过 2:审核驳回)",name = "checkSts")
    private Integer checkSts;

    @ApiModelProperty(value = "驳回详情",name = "rejectDetail")
    private String rejectDetail;
}
