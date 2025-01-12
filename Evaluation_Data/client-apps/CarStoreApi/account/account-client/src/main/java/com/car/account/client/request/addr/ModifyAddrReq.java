package com.car.account.client.request.addr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author zhangyp
 * @date 2021/1/14 23:10
 */
@Data
@ApiModel
public class ModifyAddrReq extends ReceiveAddrReq {

    /**
     * 地址主键
     */
    @NotEmpty
    @Length(max = 32)
    @ApiModelProperty(value="地址主键",name="uuid")
    private String uuid;
}
