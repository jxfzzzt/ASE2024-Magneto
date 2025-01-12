package com.car.order.client.response.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/1/27
 */
@Data
@ApiModel(value="CommentInfoListRes",description="查询评论信息列表VO")
public class CommentInfoListRes {

    @ApiModelProperty(value = "评论uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "评论详情", name = "commentDesc")
    private String commentDesc;

    @ApiModelProperty(value = "评论人", name = "userUuid")
    private String userUuid;

    @ApiModelProperty(value = "评论人名称", name = "userName")
    private String userName;

    @ApiModelProperty(value = "下单时间 yyyy-MM-dd",name="createdTime",example = "2020-12-30 21:35:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createdTime;

}
