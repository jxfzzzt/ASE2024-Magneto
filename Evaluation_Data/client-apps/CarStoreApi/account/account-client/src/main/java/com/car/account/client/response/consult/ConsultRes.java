package com.car.account.client.response.consult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 问题列表 >>> 旁听功能开发
 * @author zhangyp
 * @date 2021年1月28日00:14:07
 */
@Data
@ApiModel
public class ConsultRes {

    @ApiModelProperty(value = "uuid",name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "咨询标题",name = "title")
    private String title;

    @ApiModelProperty(value = "技师姓名",name = "technicianName")
    private String technicianName;


    @ApiModelProperty(value = "技师头像",name = "technicianImgUrl")
    private String technicianImgUrl;


    @ApiModelProperty(value = "车主姓名",name = "carOwnerName")
    private String carOwnerName;

    @ApiModelProperty(value = "车主头像",name = "carOwnerImgUrl")
    private String carOwnerImgUrl;

    @ApiModelProperty(value = "答复时间",name = "answerTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date answerTime;

    @ApiModelProperty(value = "咨询图片列表",name = "consultImgList")
    private List<String> consultImgList;

    @ApiModelProperty(value = "回答图片列表",name = "answerImgList")
    private List<String> answerImgList;

    @ApiModelProperty(value = "咨询描述",name = "consultDesc")
    private String consultDesc;

    @ApiModelProperty(value = "回答描述",name = "answerDesc")
    private String answerDesc;
}
