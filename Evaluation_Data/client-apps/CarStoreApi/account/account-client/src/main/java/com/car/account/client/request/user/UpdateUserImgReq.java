package com.car.account.client.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/10
 */
@Data
@ApiModel(value = "UpdateUserImgReq", description = "修改用户头像接收参数VO")
public class UpdateUserImgReq {

    @ApiModelProperty(value = "用户头像Url", name = "userPhotoImg")
    private String userPhotoImg;
}
