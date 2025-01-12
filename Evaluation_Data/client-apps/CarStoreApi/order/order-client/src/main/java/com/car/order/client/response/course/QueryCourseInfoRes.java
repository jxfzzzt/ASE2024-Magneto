package com.car.order.client.response.course;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 *
 * @author:  cjw
 * Date:  2021/2/21
 */
@Data
@ApiModel(value = "QueryCourseInfoRes", description = "查看课程详情返回VO")
public class QueryCourseInfoRes {

    @ApiModelProperty(value = "uuid", name = "uuid")
    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", name = "createdTime")
    private Date createdTime;

    @ApiModelProperty(value = "课程名称", name = "courseTitle")
    private String courseTitle;

    @ApiModelProperty(value = "课程分类uuid", name = "courseParentUuid")
    private String courseParentUuid;

    @ApiModelProperty(value = "课程讲师", name = "courseLecturer")
    private String courseLecturer;

    @ApiModelProperty(value = "课程类型 0:直播 1:图文", name = "courseType")
    private Integer courseType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "课程时间:直播课程时才有", name = "courseTime")
    private Date courseTime;

    @ApiModelProperty(value = "课程简介", name = "courseIntro")
    private String courseIntro;

    @ApiModelProperty(value = "课程内容:直播课程时为空  图文课程保存具体图文及视频信息", name = "courseContent")
    private String courseContent;

    @ApiModelProperty(value = "课程链接:直播课程时才有", name = "courseUrl")
    private String courseUrl;

    @ApiModelProperty(value = "课程金额", name = "courseAmount")
    private BigDecimal courseAmount;

    @ApiModelProperty(value = "课程封面", name = "courseCover")
    private String courseCover;

    @ApiModelProperty(value = "是否购买 0:购买，1:未购买")
    private Integer isPurchase;

    @ApiModelProperty(value = "最新课程", name = "latestCourse")
    private Integer latestCourse;
}
