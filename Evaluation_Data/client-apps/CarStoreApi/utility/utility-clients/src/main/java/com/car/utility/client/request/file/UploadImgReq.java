package com.car.utility.client.request.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author xlj
 */
@Data
@ApiModel
public class UploadImgReq {

    @NotBlank(message = "请选择上传图片")
    @ApiModelProperty("图片的base64字符串")
    private String base64Img;

    @NotBlank(message = "请选择上传图片类型，人员：person  其他：other")
    @ApiModelProperty("person人员 other 其他")
    private String type;
}
