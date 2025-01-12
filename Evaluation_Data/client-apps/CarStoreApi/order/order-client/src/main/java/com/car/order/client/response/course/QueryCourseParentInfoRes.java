package com.car.order.client.response.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Data
@ApiModel(value = "QueryCourseParentInfoRes", description = "查询课程分类详情返回VO")
public class QueryCourseParentInfoRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @ApiModelProperty(value = "教程名称", name = "courseTitle")
    private String courseTitle;

    @ApiModelProperty(value = "教程封面图片url", name = "courseCover")
    private String courseCover;

    @ApiModelProperty(value = "教程描述图文", name = "courseDesc")
    private String courseDesc;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;


}
